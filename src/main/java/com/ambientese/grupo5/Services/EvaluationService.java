package com.ambientese.grupo5.services;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ambientese.grupo5.exception.InsufficientQuestionsException;
import com.ambientese.grupo5.exception.InvalidCompanyIdException;
import com.ambientese.grupo5.exception.InvalidQuestionCountException;
import com.ambientese.grupo5.dto.EvaluationRequest;
import com.ambientese.grupo5.dto.EvaluationResponse;
import com.ambientese.grupo5.model.AnswerModel;
import com.ambientese.grupo5.model.CompanyModel;
import com.ambientese.grupo5.model.EvaluationModel;
import com.ambientese.grupo5.model.QuestionModel;
import com.ambientese.grupo5.model.enums.AnswersEnum;
import com.ambientese.grupo5.model.enums.CertificateLevelEnum;
import com.ambientese.grupo5.model.enums.PillarEnum;
import com.ambientese.grupo5.repository.AnswerRepository;
import com.ambientese.grupo5.repository.CompanyRepository;
import com.ambientese.grupo5.repository.EvaluationRepository;
import com.ambientese.grupo5.repository.QuestionRepository;

import jakarta.transaction.Transactional;

@Service
public class EvaluationService {
    
    private static final int REQUIRED_QUESTIONS = 30;
    private static final int QUESTIONS_PER_PILLAR = 10;


    private final EvaluationRepository evaluationRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final CompanyRepository companyRepository;

    public EvaluationService(EvaluationRepository evaluationRepository,
                             QuestionRepository questionRepository,
                             AnswerRepository answerRepository,
                             CompanyRepository companyRepository) {
        this.evaluationRepository = evaluationRepository;
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
        this.companyRepository = companyRepository;
    }

    @Transactional
    public EvaluationResponse searchQuestionsInDb(boolean isNewEvaluation, Long companyId) {
        return isNewEvaluation ? handleNewEvaluation(companyId) : handleExistingEvaluation(companyId);
    }

    private EvaluationResponse handleNewEvaluation(Long companyId) {
        deleteIncompleteEvaluationIfExists(companyId);

        List<QuestionModel> selectedQuestions = selectQuestionsForEvaluation();
        if (selectedQuestions.size() != REQUIRED_QUESTIONS) {
            throw new InsufficientQuestionsException();
        }

        return new EvaluationResponse(selectedQuestions, null);
    }

    private void deleteIncompleteEvaluationIfExists(Long companyId) {
        evaluationRepository.findIncompleteByCompanyId(companyId).ifPresent(latestEvaluation -> {
            answerRepository.deleteAll(latestEvaluation.getAnswers());
            evaluationRepository.delete(latestEvaluation);
        });
    }

    private List<QuestionModel> selectQuestionsForEvaluation() {
        List<QuestionModel> allQuestions = new ArrayList<>();
        SecureRandom secureRandom = new SecureRandom();
        for (PillarEnum pillar : PillarEnum.values()) {
            List<QuestionModel> questionsPillar = questionRepository.findByPillar(pillar);
            Collections.shuffle(questionsPillar, secureRandom);
            allQuestions.addAll(questionsPillar.subList(0, Math.min(questionsPillar.size(), QUESTIONS_PER_PILLAR)));
        }
        return allQuestions;
    }

    private EvaluationResponse handleExistingEvaluation(Long companyId) {
        return evaluationRepository.findIncompleteByCompanyId(companyId)
            .map(evaluation -> new EvaluationResponse(null, createEvaluationRequests(evaluation)))
            .orElseThrow(() -> new RuntimeException("Não foi possível encontrar o formulário"));
    }

    private List<EvaluationRequest> createEvaluationRequests(EvaluationModel evaluation) {
        return evaluation.getAnswers().stream()
            .map(answer -> new EvaluationRequest(
                answer.getQuestion().getId(),
                answer.getQuestion().getDescription(),
                answer.getAnswer(),
                answer.getQuestion().getPillar(),
                evaluation.getId()
            ))
            .toList();
    }

    @Transactional
    public EvaluationModel createEvaluation(Long companyId, List<EvaluationRequest> evaluationRequests, boolean isComplete) {
        Optional<EvaluationModel> incompleteEvaluationOpt = evaluationRepository.findIncompleteByCompanyId(companyId);

        if (isComplete) {
            validateCompleteEvaluation(companyId, evaluationRequests);
            return incompleteEvaluationOpt
                .map(evaluation -> replaceIncompleteEvaluationWithComplete(evaluation, evaluationRequests))
                .orElseGet(() -> createCompleteEvaluation(companyId, evaluationRequests));
        } else {
            return incompleteEvaluationOpt
                .map(evaluation -> replaceIncompleteEvaluationWithIncomplete(evaluation, evaluationRequests))
                .orElseGet(() -> createIncompleteEvaluation(companyId, evaluationRequests));
        }
    }

    private EvaluationModel createCompleteEvaluation(Long companyId, List<EvaluationRequest> evaluationRequests) {
        CompanyModel company = companyRepository.findById(companyId)
            .orElseThrow(InvalidCompanyIdException::new);

        EvaluationModel evaluationModel = new EvaluationModel();
        evaluationModel.setCompany(company);
        evaluationModel = updateScores(evaluationModel, evaluationRequests);

        List<AnswerModel> answers = createAnswers(evaluationModel, evaluationRequests);
        answerRepository.saveAll(answers);
        evaluationModel.setAnswers(answers);
        evaluationRepository.save(evaluationModel);

        updateRanking();

        return evaluationModel;
    }

    private EvaluationModel createIncompleteEvaluation(Long companyId, List<EvaluationRequest> evaluationRequests) {
        CompanyModel company = companyRepository.findById(companyId)
            .orElseThrow(() -> new RuntimeException("Empresa não encontrada"));

        EvaluationModel evaluationModel = new EvaluationModel();
        evaluationModel.setCompany(company);
        evaluationModel.setAnswerDate(new Date());
        evaluationModel = evaluationRepository.saveAndFlush(evaluationModel);

        List<AnswerModel> answers = createAnswers(evaluationModel, evaluationRequests);
        answerRepository.saveAll(answers);
        evaluationModel.setAnswers(answers);

        return evaluationRepository.save(evaluationModel);
    }

    private EvaluationModel replaceIncompleteEvaluationWithComplete(EvaluationModel evaluation, List<EvaluationRequest> evaluationRequests) {
        clearExistingAnswers(evaluation);
        updateScores(evaluation, evaluationRequests);
        evaluation.setAnswerDate(new Date());

        List<AnswerModel> newAnswers = createAnswers(evaluation, evaluationRequests);
        answerRepository.saveAll(newAnswers);
        evaluation.setAnswers(newAnswers);
        evaluationRepository.saveAndFlush(evaluation);

        updateRanking();

        return evaluation;
    }

    private EvaluationModel replaceIncompleteEvaluationWithIncomplete(EvaluationModel evaluation, List<EvaluationRequest> evaluationRequests) {
        clearExistingAnswers(evaluation);
        evaluation.setAnswerDate(new Date());

        List<AnswerModel> newAnswers = createAnswers(evaluation, evaluationRequests);
        answerRepository.saveAll(newAnswers);
        evaluation.setAnswers(newAnswers);

        return evaluationRepository.saveAndFlush(evaluation);
    }

    private void clearExistingAnswers(EvaluationModel evaluation) {
        if (!evaluation.getAnswers().isEmpty()) {
            evaluation.setAnswers(new ArrayList<>());
            evaluationRepository.saveAndFlush(evaluation);
            answerRepository.deleteAll(evaluation.getAnswers());
        }
    }

    private List<AnswerModel> createAnswers(EvaluationModel evaluation, List<EvaluationRequest> evaluationRequests) {
        return evaluationRequests.stream()
            .map(request -> {
                QuestionModel question = questionRepository.findById(request.getQuestionId())
                    .orElseThrow(() -> new RuntimeException("Pergunta não encontrada"));
                return new AnswerModel(evaluation, question, request.getUserAnswer());
            })
            .toList();
    }

    private void updateRanking() {
        List<EvaluationModel> latestEvaluations = evaluationRepository.findLatestByCompanyOrderByFinalScoreDesc();
        for (int i = 0; i < latestEvaluations.size(); i++) {
            CompanyModel company = latestEvaluations.get(i).getCompany();
            company.setRanking(i + 1);
            companyRepository.save(company);
        }
    }

    private void validateCompleteEvaluation(Long companyId, List<EvaluationRequest> evaluationRequests) {
        if (evaluationRequests.size() != REQUIRED_QUESTIONS) {
            throw new InvalidQuestionCountException();
        }
        if (companyId == null) {
            throw new InvalidCompanyIdException();
        }
    }

    public boolean haveActiveEvaluation(Long companyId) {
        EvaluationModel latestEvaluation = evaluationRepository.findLatestEvaluationByCompanyId(companyId);
        return latestEvaluation != null && latestEvaluation.getFinalScore() == null;
    }

    public EvaluationModel searchById(Long id) {
        return evaluationRepository.findById(id).orElse(null);
    }

    private EvaluationModel updateScores(EvaluationModel evaluationModel, List<EvaluationRequest> evaluationRequests) {
        ScoreCalculator calculator = new ScoreCalculator(evaluationRequests);
        evaluationModel.setFinalScore(calculator.getFinalScore());
        evaluationModel.setSocialScore(calculator.getSocialScore());
        evaluationModel.setEnviornmentalScore(calculator.getEnvironmentalScore());
        evaluationModel.setGovernmentScore(calculator.getGovernmentScore());
        evaluationModel.setCertificate(calculateCertificateLevel(calculator.getFinalScore()));
        evaluationModel.setAnswerDate(new Date());
        return evaluationRepository.saveAndFlush(evaluationModel);
    }

    private CertificateLevelEnum calculateCertificateLevel(double finalScore) {
        if (finalScore >= 100) {
            return CertificateLevelEnum.Ouro;
        } else if (finalScore >= 75.1) {
            return CertificateLevelEnum.Prata;
        } else {
            return CertificateLevelEnum.Bronze;
        }
    }

    private static class ScoreCalculator {
        private final int totalQuestions;
        private final int questionsAsPer;
        private final PillarScores pillarScores;

        public ScoreCalculator(List<EvaluationRequest> requests) {
            this.pillarScores = new PillarScores();
            int totalValid = 0;
            int compliant = 0;

            for (EvaluationRequest request : requests) {
                if (request.getUserAnswer() == null || request.getUserAnswer() == AnswersEnum.NaoSeAdequa) 
                    continue;

                totalValid++;
                if (request.getUserAnswer() == AnswersEnum.Conforme) {
                    compliant++;
                    pillarScores.incrementCompliant(request.getQuestionPillar());
                } else if (request.getUserAnswer() == AnswersEnum.NaoConforme) {
                    pillarScores.incrementNonCompliant(request.getQuestionPillar());
                }
            }

            this.totalQuestions = totalValid;
            this.questionsAsPer = compliant;
        }

        public int getFinalScore() {
            return calculatePercentage(questionsAsPer, totalQuestions);
        }

        public int getSocialScore() {
            return pillarScores.getScore(PillarEnum.Social);
        }

        public int getEnvironmentalScore() {
            return pillarScores.getScore(PillarEnum.Ambiental);
        }

        public int getGovernmentScore() {
            return pillarScores.getScore(PillarEnum.Governamental);
        }

        private int calculatePercentage(int value, int total) {
            return total > 0 ? (int) ((double) value / total * 100.0) : 0;
        }
    }

    private static class PillarScores {
        private final int[] compliant = new int[PillarEnum.values().length];
        private final int[] nonCompliant = new int[PillarEnum.values().length];

        public void incrementCompliant(PillarEnum pillar) {
            compliant[pillar.ordinal()]++;
        }

        public void incrementNonCompliant(PillarEnum pillar) {
            nonCompliant[pillar.ordinal()]++;
        }

        public int getScore(PillarEnum pillar) {
            int index = pillar.ordinal();
            int total = compliant[index] + nonCompliant[index];
            return total > 0 ? (int) ((double) compliant[index] / total * 100.0) : 0;
        }
    }
}
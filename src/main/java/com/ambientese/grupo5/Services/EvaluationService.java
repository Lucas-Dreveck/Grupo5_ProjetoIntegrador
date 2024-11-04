package com.ambientese.grupo5.Services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ambientese.grupo5.DTO.EvaluationRequest;
import com.ambientese.grupo5.DTO.EvaluationResponse;
import com.ambientese.grupo5.Model.AnswerModel;
import com.ambientese.grupo5.Model.CompanyModel;
import com.ambientese.grupo5.Model.EvaluationModel;
import com.ambientese.grupo5.Model.QuestionModel;
import com.ambientese.grupo5.Model.Enums.AnswersEnum;
import com.ambientese.grupo5.Model.Enums.CertificateLevelEnum;
import com.ambientese.grupo5.Model.Enums.PillarEnum;
import com.ambientese.grupo5.Repository.AnswerRepository;
import com.ambientese.grupo5.Repository.CompanyRepository;
import com.ambientese.grupo5.Repository.EvaluationRepository;
import com.ambientese.grupo5.Repository.QuestionRepository;

import jakarta.transaction.Transactional;
@Service
public class EvaluationService {

    private static final int REQUIRED_QUESTIONS = 30;
    private static final int QUESTIONS_PER_PILLAR = 10;

    @Autowired
    private EvaluationRepository evaluationRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Transactional
    public EvaluationResponse searchQuestionsInDb(boolean isNewEvaluation, Long companyId) {
        return isNewEvaluation ? handleNewEvaluation(companyId) : handleExistingEvaluation(companyId);
    }

    private EvaluationResponse handleNewEvaluation(Long companyId) {
        deleteIncompleteEvaluationIfExists(companyId);

        List<QuestionModel> selectedQuestions = selectQuestionsForEvaluation();
        if (selectedQuestions.size() != REQUIRED_QUESTIONS) {
            throw new RuntimeException("Número insuficiente de questões encontradas");
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
        Random random = new Random();
        for (PillarEnum pillar : PillarEnum.values()) {
            List<QuestionModel> questionsPillar = questionRepository.findByPillar(pillar);
            Collections.shuffle(questionsPillar, random);
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
            .collect(Collectors.toList());
    }

    @Transactional
    public EvaluationModel createEvaluation(Long companyId, List<EvaluationRequest> evaluationRequests, Boolean isComplete) {
        return evaluationRepository.findIncompleteByCompanyId(companyId)
            .map(existingEvaluation -> updateEvaluation(existingEvaluation, evaluationRequests, isComplete))
            .orElseGet(() -> createNewEvaluation(companyId, evaluationRequests, isComplete));
    }

    private EvaluationModel createNewEvaluation(Long companyId, List<EvaluationRequest> requests, boolean isComplete) {
        CompanyModel company = companyRepository.findById(companyId)
            .orElseThrow(() -> new RuntimeException("Empresa não encontrada"));

        EvaluationModel evaluation = new EvaluationModel();
        evaluation.setCompany(company);
        evaluation.setAnswerDate(new Date());
        evaluation = evaluationRepository.saveAndFlush(evaluation);

        updateEvaluationAttributes(evaluation, requests, isComplete);

        return evaluation;
    }

    private EvaluationModel updateEvaluation(EvaluationModel evaluation, List<EvaluationRequest> requests, boolean isComplete) {
        clearExistingAnswers(evaluation);
        updateEvaluationAttributes(evaluation, requests, isComplete);
        return evaluation;
    }

    private void updateEvaluationAttributes(EvaluationModel evaluation, List<EvaluationRequest> requests, boolean isComplete) {
        List<AnswerModel> answers = createAnswers(evaluation, requests);
        answerRepository.saveAll(answers);
        evaluation.setAnswers(answers);
        
        if (isComplete) {
            validateCompleteEvaluation(evaluation.getCompany().getId(), requests);
            calculateAndSetScores(evaluation, requests);
            updateRanking();
        }
        
        evaluationRepository.save(evaluation);
    }

    private void clearExistingAnswers(EvaluationModel evaluation) {
        if (!evaluation.getAnswers().isEmpty()) {
            answerRepository.deleteAll(evaluation.getAnswers());
            evaluation.setAnswers(new ArrayList<>());
            evaluationRepository.saveAndFlush(evaluation);
        }
    }

    private List<AnswerModel> createAnswers(EvaluationModel evaluation, List<EvaluationRequest> requests) {
        return requests.stream()
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

    private void validateCompleteEvaluation(Long companyId, List<EvaluationRequest> requests) {
        if (requests.size() != REQUIRED_QUESTIONS) {
            throw new RuntimeException("Número de questões inválido");
        }
        if (companyId == null) {
            throw new RuntimeException("ID da empresa inválido");
        }
    }

    public boolean haveActiveEvaluation(Long companyId) {
        EvaluationModel latestEvaluation = evaluationRepository.findLatestEvaluationByCompanyId(companyId);
        return latestEvaluation != null && latestEvaluation.getFinalScore() == null;
    }

    public EvaluationModel searchById(Long id) {
        return evaluationRepository.findById(id).orElse(null);
    }

    private void calculateAndSetScores(EvaluationModel evaluation, List<EvaluationRequest> requests) {
        ScoreCalculator calculator = new ScoreCalculator(requests);
        evaluation.setFinalScore(calculator.getFinalScore());
        evaluation.setSocialScore(calculator.getSocialScore());
        evaluation.setEnviornmentalScore(calculator.getEnvironmentalScore());
        evaluation.setGovernmentScore(calculator.getGovernmentScore());
        evaluation.setCertificate(calculateCertificateLevel(calculator.getFinalScore()));
        evaluation.setAnswerDate(new Date());
    }

    private CertificateLevelEnum calculateCertificateLevel(int finalScore) {
        if (finalScore >= 100) return CertificateLevelEnum.Ouro;
        if (finalScore >= 75.1) return CertificateLevelEnum.Prata;
        return CertificateLevelEnum.Bronze;
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
                if (request.getUserAnswer() == AnswersEnum.NaoSeAdequa) continue;

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

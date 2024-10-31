package com.ambientese.grupo5.Services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ambientese.grupo5.DTO.EvaluationRequest;
import com.ambientese.grupo5.DTO.EvaluationResponse;
import com.ambientese.grupo5.Exception.QuestionsNotFoundException;
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

    @Autowired
    private EvaluationRepository evaluationRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Transactional
    public EvaluationResponse searchQuestionsInDb(Boolean isNewEvaluation, Long companyId) {
        if (isNewEvaluation) {
            Random random = new Random();
            Optional<EvaluationModel> latestEvaluation = evaluationRepository.findIncompleteByCompanyId(companyId);
            if (latestEvaluation.isPresent()) {
                answerRepository.deleteAll(latestEvaluation.get().getAnswers());
                evaluationRepository.delete(latestEvaluation.get());
            }

            List<QuestionModel> allQuestions = new ArrayList<>();
            for (PillarEnum pillar : PillarEnum.values()) {
                List<QuestionModel> questionsPillar = questionRepository.findByPillar(pillar);

                Collections.shuffle(questionsPillar, random);

                allQuestions.addAll(questionsPillar.subList(0, Math.min(questionsPillar.size(), 10)));
            }
            if (allQuestions.size() != 30) {
                throw new QuestionsNotFoundException("Não foi possível encontrar o número necessário de questions");
            }
            return new EvaluationResponse(allQuestions, null);
        } else {
            Optional<EvaluationModel> latestEvaluation = evaluationRepository.findIncompleteByCompanyId(companyId);
            if (latestEvaluation.isPresent()) {
                List<EvaluationRequest> evaluationRequests = latestEvaluation.get().getAnswers().stream()
                    .map(answer -> new EvaluationRequest(
                        answer.getQuestion().getId(),
                        answer.getQuestion().getDescription(),
                        answer.getAnswer(),
                        answer.getQuestion().getPillar(),
                        latestEvaluation.get().getId()
                    ))
                    .collect(Collectors.toList());
                return new EvaluationResponse(null, evaluationRequests);
            } else {
                throw new RuntimeException("Não foi possível encontrar o formulário");
            }
        }
    }

    @Transactional
    public EvaluationModel createEvaluation(Long companyId, List<EvaluationRequest> evaluationRequestList, Boolean isComplete) {
        Optional<EvaluationModel> incompleteEvaluationOpt = evaluationRepository.findIncompleteByCompanyId(companyId);

        if (isComplete) {
            if (incompleteEvaluationOpt.isPresent()) {
                return replaceIncompleteEvaluationWithComplete(incompleteEvaluationOpt.get(), evaluationRequestList);
            } else {
                return createCompleteEvaluation(companyId, evaluationRequestList);
            }
        } else {
            if (incompleteEvaluationOpt.isPresent()) {
                return replaceIncompleteEvaluationWithIncomplete(incompleteEvaluationOpt.get(), evaluationRequestList);
            } else {
                return createIncompleteEvaluation(companyId, evaluationRequestList);
            }
        }
    }

    public EvaluationModel createCompleteEvaluation(Long companyId, List<EvaluationRequest> evaluationRequestList) {
        validateCompleteEvaluation(companyId, evaluationRequestList);

        CompanyModel company = companyRepository.findById(companyId).orElseThrow(() -> new RuntimeException("Empresa não encontrada"));

        EvaluationModel evaluationModel = new EvaluationModel();
        evaluationModel.setCompany(company);

        evaluationModel = updateScores(evaluationModel, evaluationRequestList);

        List<AnswerModel> answers = createAnswers(evaluationModel, evaluationRequestList);
        answerRepository.saveAll(answers);
        evaluationModel.setAnswers(answers);
        evaluationRepository.save(evaluationModel);

        updateRanking();

        return evaluationModel;
    }

    public EvaluationModel createIncompleteEvaluation(Long companyId, List<EvaluationRequest> evaluationRequestList) {
        CompanyModel company = companyRepository.findById(companyId).orElseThrow(() -> new RuntimeException("Empresa não encontrada"));

        EvaluationModel evaluationModel = new EvaluationModel();
        evaluationModel.setCompany(company);
        evaluationModel.setAnswerDate(new Date());

        evaluationModel = evaluationRepository.saveAndFlush(evaluationModel);

        List<AnswerModel> answers = createAnswers(evaluationModel, evaluationRequestList);
        answerRepository.saveAll(answers);
        evaluationModel.setAnswers(answers);

        return evaluationRepository.save(evaluationModel);
    }

    private EvaluationModel replaceIncompleteEvaluationWithComplete(EvaluationModel incompleteEvaluation, List<EvaluationRequest> evaluationRequestList) {
        // Remover todas as answers antigas do formulário incompleto
        List<AnswerModel> oldAnswers = incompleteEvaluation.getAnswers();
        if (oldAnswers != null && !oldAnswers.isEmpty()) {
            incompleteEvaluation.setAnswers(new ArrayList<>());
            evaluationRepository.saveAndFlush(incompleteEvaluation);
            answerRepository.deleteAll(oldAnswers);
        }

        // Atualizar o formulário incompleto para se tornar completo
        updateScores(incompleteEvaluation, evaluationRequestList);
        incompleteEvaluation.setAnswerDate(new Date());

        List<AnswerModel> newAnswers = createAnswers(incompleteEvaluation, evaluationRequestList);
        answerRepository.saveAll(newAnswers);
        incompleteEvaluation.setAnswers(newAnswers);
        evaluationRepository.saveAndFlush(incompleteEvaluation);

        updateRanking();

        return incompleteEvaluation;
    }

    private EvaluationModel replaceIncompleteEvaluationWithIncomplete(EvaluationModel incompleteEvaluation, List<EvaluationRequest> evaluationRequestList) {
        // Remover todas as answers antigas do formulário incompleto
        List<AnswerModel> oldAnswers = incompleteEvaluation.getAnswers();
        if (oldAnswers != null && !oldAnswers.isEmpty()) {
            incompleteEvaluation.setAnswers(new ArrayList<>());
            evaluationRepository.saveAndFlush(incompleteEvaluation);
            answerRepository.deleteAll(oldAnswers);
        }

        // Atualizar o formulário incompleto com novas answers
        incompleteEvaluation.setAnswerDate(new Date());

        List<AnswerModel> newAnswers = createAnswers(incompleteEvaluation, evaluationRequestList);
        answerRepository.saveAll(newAnswers);
        incompleteEvaluation.setAnswers(newAnswers);

        return evaluationRepository.saveAndFlush(incompleteEvaluation);
    }

    private EvaluationModel updateScores(EvaluationModel evaluationModel, List<EvaluationRequest> evaluationRequestList) {
        int totalQuestions = evaluationRequestList.size();
        int questionsAsPer = 0;
        int socialAsPer = 0;
        int nonCompliantSocial = 0;
        int enviornmentalAsPer = 0;
        int nonCompliantEnviornmental = 0;
        int governmentAsPer = 0;
        int nonCompliantGovernment = 0;

        for (EvaluationRequest answer : evaluationRequestList) {
            if (answer.getUserAnswer() != null) {
                if (answer.getUserAnswer() == AnswersEnum.Conforme) {
                    questionsAsPer++;
                    switch (answer.getQuestionPillar()) {
                        case Social:
                            socialAsPer++;
                            break;
                        case Ambiental:
                            enviornmentalAsPer++;
                            break;
                        case Governamental:
                            governmentAsPer++;
                            break;
                    }
                } else if (answer.getUserAnswer() == AnswersEnum.NaoConforme) {
                    switch (answer.getQuestionPillar()) {
                        case Social:
                            nonCompliantSocial++;
                            break;
                        case Ambiental:
                            nonCompliantEnviornmental++;
                            break;
                        case Governamental:
                            nonCompliantGovernment++;
                            break;
                    }
                } else if (answer.getUserAnswer() == AnswersEnum.NaoSeAdequa) {
                    totalQuestions--;
                }
            }
        }

        double finalScore = (double) questionsAsPer / totalQuestions * 100.0;
        double socialScore = (double) socialAsPer / (socialAsPer + nonCompliantSocial) * 100.0;
        double enviornmentalScore = (double) enviornmentalAsPer / (enviornmentalAsPer + nonCompliantEnviornmental) * 100.0;
        double governmentScore = (double) governmentAsPer / (governmentAsPer + nonCompliantGovernment) * 100.0;

        CertificateLevelEnum nivelCertificado = calculateCertificateLevel(finalScore);

        evaluationModel.setFinalScore((int) finalScore);
        evaluationModel.setSocialScore((int) socialScore);
        evaluationModel.setEnviornmentalScore((int) enviornmentalScore);
        evaluationModel.setGovernmentScore((int) governmentScore);
        evaluationModel.setCertificate(nivelCertificado);
        evaluationModel.setAnswerDate(new Date());
        return evaluationRepository.saveAndFlush(evaluationModel);
    }

    private List<AnswerModel> createAnswers(EvaluationModel evaluationModel, List<EvaluationRequest> evaluationRequestList) {
        List<AnswerModel> answers = new ArrayList<>();
        for (EvaluationRequest entry : evaluationRequestList) {
            QuestionModel question = questionRepository.findById(entry.getQuestionId())
                    .orElseThrow(() -> new RuntimeException("Pergunta não encontrada"));
            AnswerModel answerModel = new AnswerModel(evaluationModel, question, entry.getUserAnswer());
            answers.add(answerModel);
        }
        return answers;
    }

    private void updateRanking() {
        List<EvaluationModel> latestEvaluations = evaluationRepository.findLatestByCompanyOrderByFinalScoreDesc();

        for (int i = 0; i < latestEvaluations.size(); i++) {
            EvaluationModel evaluation = latestEvaluations.get(i);
            CompanyModel company = evaluation.getCompany();
            company.setRanking(i + 1);
            companyRepository.save(company);
        }
    }

    public CertificateLevelEnum calculateCertificateLevel(double finalScore) {
        if (finalScore >= 100) {
            return CertificateLevelEnum.Ouro;
        } else if (finalScore >= 75.1) {
            return CertificateLevelEnum.Prata;
        } else {
            return CertificateLevelEnum.Bronze;
        }
    }

    private void validateCompleteEvaluation(Long companyId, List<EvaluationRequest> evaluationRequestList) {
        if (evaluationRequestList.size() != 30) {
            throw new IllegalArgumentException("Número de questions inválido");
        }
        if (companyId == null) {
            throw new IllegalArgumentException("ID da company inválido");
        }
    }

    public boolean haveActiveEvaluation(Long companyId) {
        EvaluationModel latestEvaluation = evaluationRepository.findLatestEvaluationByCompanyId(companyId);
        if (latestEvaluation != null) {
            return latestEvaluation.getFinalScore() == null;
        }
        
        return false;
    }

    public EvaluationModel searchById(Long id) {
        return evaluationRepository.findById(id).orElse(null);
    }
}
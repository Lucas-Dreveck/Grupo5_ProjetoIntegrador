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

import com.ambientese.grupo5.DTO.FormRequest;
import com.ambientese.grupo5.DTO.FormResponse;
import com.ambientese.grupo5.Model.AnswerModel;
import com.ambientese.grupo5.Model.CompanyModel;
import com.ambientese.grupo5.Model.FormModel;
import com.ambientese.grupo5.Model.QuestionModel;
import com.ambientese.grupo5.Model.Enums.AnswersEnum;
import com.ambientese.grupo5.Model.Enums.CertificateLevelEnum;
import com.ambientese.grupo5.Model.Enums.PillarEnum;
import com.ambientese.grupo5.Repository.AnswerRepository;
import com.ambientese.grupo5.Repository.CompanyRepository;
import com.ambientese.grupo5.Repository.FormRepository;
import com.ambientese.grupo5.Repository.QuestionRepository;

import jakarta.transaction.Transactional;

@Service
public class FormService {

    @Autowired
    private FormRepository formRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Transactional
    public FormResponse searchQuestionsInDb(Boolean isNewForm, Long companyId) {
        if (isNewForm) {
            Random random = new Random();
            Optional<FormModel> latestForm = formRepository.findIncompleteByCompanyId(companyId);
            if (latestForm.isPresent()) {
                answerRepository.deleteAll(latestForm.get().getAnswers());
                formRepository.delete(latestForm.get());
            }

            List<QuestionModel> allQuestions = new ArrayList<>();
            for (PillarEnum pillar : PillarEnum.values()) {
                List<QuestionModel> questionsPillar = questionRepository.findByPillar(pillar);

                Collections.shuffle(questionsPillar, random);

                allQuestions.addAll(questionsPillar.subList(0, Math.min(questionsPillar.size(), 10)));
            }
            if (allQuestions.size() != 30) {
                throw new RuntimeException("Não foi possível encontrar o número necessário de questions");
            }
            return new FormResponse(allQuestions, null);
        } else {
            Optional<FormModel> latestForm = formRepository.findIncompleteByCompanyId(companyId);
            if (latestForm.isPresent()) {
                List<FormRequest> formRequests = latestForm.get().getAnswers().stream()
                    .map(answer -> new FormRequest(
                        answer.getQuestion().getId(),
                        answer.getQuestion().getDescription(),
                        answer.getAnswer(),
                        answer.getQuestion().getPillar(),
                        latestForm.get().getId()
                    ))
                    .collect(Collectors.toList());
                return new FormResponse(null, formRequests);
            } else {
                throw new RuntimeException("Não foi possível encontrar o formulário");
            }
        }
    }

    @Transactional
    public FormModel createForm(Long companyId, List<FormRequest> formRequestList, Boolean isComplete) {
        Optional<FormModel> incompleteFormOpt = formRepository.findIncompleteByCompanyId(companyId);

        if (isComplete) {
            if (incompleteFormOpt.isPresent()) {
                return replaceIncompleteFormWithComplete(incompleteFormOpt.get(), formRequestList);
            } else {
                return createCompleteForm(companyId, formRequestList);
            }
        } else {
            if (incompleteFormOpt.isPresent()) {
                return replaceIncompleteFormWithIncomplete(incompleteFormOpt.get(), formRequestList);
            } else {
                return createIncompleteForm(companyId, formRequestList);
            }
        }
    }

    public FormModel createCompleteForm(Long companyId, List<FormRequest> formRequestList) {
        validateCompleteForm(companyId, formRequestList);

        CompanyModel company = companyRepository.findById(companyId).orElseThrow(() -> new RuntimeException("Empresa não encontrada"));

        FormModel formModel = new FormModel();
        formModel.setCompany(company);

        formModel = updateScores(formModel, formRequestList);

        List<AnswerModel> answers = createAnswers(formModel, formRequestList);
        answerRepository.saveAll(answers);
        formModel.setAnswers(answers);
        formRepository.save(formModel);

        updateRanking();

        return formModel;
    }

    public FormModel createIncompleteForm(Long companyId, List<FormRequest> formRequestList) {
        CompanyModel company = companyRepository.findById(companyId).orElseThrow(() -> new RuntimeException("Empresa não encontrada"));

        FormModel formModel = new FormModel();
        formModel.setCompany(company);
        formModel.setAnswerDate(new Date());

        formModel = formRepository.saveAndFlush(formModel);

        List<AnswerModel> answers = createAnswers(formModel, formRequestList);
        answerRepository.saveAll(answers);
        formModel.setAnswers(answers);

        return formRepository.save(formModel);
    }

    private FormModel replaceIncompleteFormWithComplete(FormModel incompleteForm, List<FormRequest> formRequestList) {
        // Remover todas as answers antigas do formulário incompleto
        List<AnswerModel> oldAnswers = incompleteForm.getAnswers();
        if (oldAnswers != null && !oldAnswers.isEmpty()) {
            incompleteForm.setAnswers(new ArrayList<>());
            formRepository.saveAndFlush(incompleteForm);
            answerRepository.deleteAll(oldAnswers);
        }

        // Atualizar o formulário incompleto para se tornar completo
        updateScores(incompleteForm, formRequestList);
        incompleteForm.setAnswerDate(new Date());

        List<AnswerModel> newAnswers = createAnswers(incompleteForm, formRequestList);
        answerRepository.saveAll(newAnswers);
        incompleteForm.setAnswers(newAnswers);
        formRepository.saveAndFlush(incompleteForm);

        updateRanking();

        return incompleteForm;
    }

    private FormModel replaceIncompleteFormWithIncomplete(FormModel incompleteForm, List<FormRequest> formRequestList) {
        // Remover todas as answers antigas do formulário incompleto
        List<AnswerModel> oldAnswers = incompleteForm.getAnswers();
        if (oldAnswers != null && !oldAnswers.isEmpty()) {
            incompleteForm.setAnswers(new ArrayList<>());
            formRepository.saveAndFlush(incompleteForm);
            answerRepository.deleteAll(oldAnswers);
        }

        // Atualizar o formulário incompleto com novas answers
        incompleteForm.setAnswerDate(new Date());

        List<AnswerModel> newAnswers = createAnswers(incompleteForm, formRequestList);
        answerRepository.saveAll(newAnswers);
        incompleteForm.setAnswers(newAnswers);

        return formRepository.saveAndFlush(incompleteForm);
    }

    private FormModel updateScores(FormModel formModel, List<FormRequest> formRequestList) {
        int totalQuestions = formRequestList.size();
        int questionsAsPer = 0;
        int socialAsPer = 0;
        int nonCompliantSocial = 0;
        int enviornmentalAsPer = 0;
        int nonCompliantEnviornmental = 0;
        int governmentAsPer = 0;
        int nonCompliantGovernment = 0;

        for (FormRequest answer : formRequestList) {
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

        formModel.setFinalScore((int) finalScore);
        formModel.setSocialScore((int) socialScore);
        formModel.setEnviornmentalScore((int) enviornmentalScore);
        formModel.setGovernmentScore((int) governmentScore);
        formModel.setCertificate(nivelCertificado);
        formModel.setAnswerDate(new Date());
        return formRepository.saveAndFlush(formModel);
    }

    private List<AnswerModel> createAnswers(FormModel formModel, List<FormRequest> formRequestList) {
        List<AnswerModel> answers = new ArrayList<>();
        for (FormRequest entry : formRequestList) {
            QuestionModel question = questionRepository.findById(entry.getQuestionId())
                    .orElseThrow(() -> new RuntimeException("Pergunta não encontrada"));
            AnswerModel answerModel = new AnswerModel(formModel, question, entry.getUserAnswer());
            answers.add(answerModel);
        }
        return answers;
    }

    private void updateRanking() {
        List<FormModel> latestForms = formRepository.findLatestByCompanyOrderByFinalScoreDesc();

        for (int i = 0; i < latestForms.size(); i++) {
            FormModel form = latestForms.get(i);
            CompanyModel company = form.getCompany();
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

    private void validateCompleteForm(Long companyId, List<FormRequest> formRequestList) {
        if (formRequestList.size() != 30) {
            throw new RuntimeException("Número de questions inválido");
        }
        if (companyId == null) {
            throw new RuntimeException("ID da company inválido");
        }
    }

    public boolean haveActiveForm(Long companyId) {
        FormModel latestForm = formRepository.findLatestFormByCompanyId(companyId);
        if (latestForm != null) {
            return latestForm.getFinalScore() == null;
        }
        
        return false;
    }

    public FormModel searchById(Long id) {
        return formRepository.findById(id).orElse(null);
    }
}
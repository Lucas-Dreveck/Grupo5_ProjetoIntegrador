package com.ambientese.grupo5;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import org.apache.commons.lang3.ArrayUtils;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ambientese.grupo5.DTO.EvaluationRequest;
import com.ambientese.grupo5.Model.*;
import com.ambientese.grupo5.Model.Enums.*;
import com.ambientese.grupo5.Repository.*;
import com.ambientese.grupo5.Services.EvaluationService;
import com.github.javafaker.Faker;

@Component
public class InitialDataLoader implements CommandLineRunner {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private EvaluationService evaluationService;

    private final Faker faker = new Faker(new Locale("pt-BR"));

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        UserModel rootUser = userRepository.findByLogin("root");

        if (rootUser == null) {
            int numberToGenerate = 60;

            // Populate address and company tables
            for (int i = 0; i < numberToGenerate; i++) {
                CompanyModel company = new CompanyModel();
                company.setTradeName(getRandomTradeName());
                company.setApplicantsName(faker.name().fullName());
                String cellPhone = faker.phoneNumber().cellPhone();
                if (cellPhone.length() > 15) {
                    cellPhone = cellPhone.substring(0, 15);
                }
                company.setApplicantsPhone(cellPhone);
                company.setCompanyName(faker.company().name());
                company.setCnpj(faker.number().digits(14));
                company.setSocialInscription(faker.number().digits(20));
                company.setEmail(faker.internet().emailAddress());
                String phone = faker.phoneNumber().phoneNumber();
                if (phone.length() > 15) {
                    phone = phone.substring(0, 15);
                }
                company.setCompanyPhone(phone);
                company.setSegment(getRandomSegment());
                company.setCompanySize(getRandomCompanySize());

                AddressModel address = new AddressModel();
                address.setCep(faker.address().zipCode());
                address.setNumber(faker.number().numberBetween(1, 1000));
                address.setPatio(faker.address().streetName());
                address.setComplement(faker.address().secondaryAddress());
                address.setCity(faker.address().cityName());
                address.setNeighborhood(faker.address().streetName());
                address.setUF(faker.address().stateAbbr());

                company.setAddres(address);

                companyRepository.save(company);
            }

            // Populate role table
            RoleModel managerRole = new RoleModel();
            managerRole.setDescription("Gestor");
            managerRole = roleRepository.save(managerRole);

            RoleModel consultantRole = new RoleModel();
            consultantRole.setDescription("Consultor");
            consultantRole = roleRepository.save(consultantRole);

            // Populate employee table
            for (int i = 0; i < numberToGenerate; i++) {
                UserModel user = new UserModel();
                user.setLogin(i == 0 ? "Gestor" : i == 1 ? "Consultor" : faker.name().username());
                user.setPassword(BCrypt.hashpw(i == 0 ? "gestor" : i == 1 ? "consultor" : faker.internet().password(), BCrypt.gensalt()));
                user.setIsAdmin(false);
                user = userRepository.save(user);

                EmployeeModel employee = new EmployeeModel();
                employee.setUser(user);
                employee.setName(faker.name().fullName());
                employee.setCpf(faker.regexify("\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}"));
                employee.setEmail(i == 0 ? "Gestor@test.com" : i == 1 ? "Consultor@test.com" : faker.internet().emailAddress());

                // Convert date to localDate
                Date birthday = faker.date().birthday();
                LocalDate localDate = birthday.toInstant()
                                            .atZone(ZoneId.systemDefault())
                                            .toLocalDate();
                employee.setBirthDate(localDate);
                employee.setRole(i == 0 ? managerRole : consultantRole);

                employeeRepository.save(employee);
            }

            // Populate question table
            for (PillarEnum pillar : PillarEnum.values()) {
                String[] questions;
                switch (pillar) {
                    case Ambiental:
                        questions = enviornmentalQuestions;
                        break;
                    case Social:
                        questions = socialQuestions;
                        break;
                    case Governamental:
                        questions = governmentQuestions;
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + pillar);
                }
                for (int i = 0; i < questions.length; i++) {
                    QuestionModel question = new QuestionModel();
                    question.setDescription(questions[i]);
                    question.setPillar(pillar);
                    questionRepository.save(question);
                }
            }

            // Populate evaluation table with evaluationService
            List<CompanyModel> companies = companyRepository.findAll();
            for (CompanyModel company : companies) {
                List<EvaluationRequest> evaluationRequests = generateEvaluationRequest();
                evaluationService.createEvaluation(company.getId(), evaluationRequests, true);
            }

            // Create root user
            UserModel newUser = new UserModel();
            newUser.setLogin("root");
            newUser.setPassword(BCrypt.hashpw("root", BCrypt.gensalt()));
            newUser.setIsAdmin(true);

            userRepository.save(newUser);

            System.out.println("Banco pré preenchido com sucesso.");
            System.out.println("Usuários para debug criados com sucesso:");
            System.out.println("-- Usuário root");
            System.out.println("- Login: root");
            System.out.println("- Senha: root");
            System.out.println("-- Usuário gestor");
            System.out.println("- Login: Gestor");
            System.out.println("- Senha: gestor");
            System.out.println("-- Usuário consultor");
            System.out.println("- Login: Consultor");
            System.out.println("- Senha: consultor");
        } else {
            System.out.println("Usuários para debug:");
            System.out.println("-- Usuário root");
            System.out.println("- Login: root");
            System.out.println("- Senha: root");
            System.out.println("-- Usuário gestor");
            System.out.println("- Login: Gestor");
            System.out.println("- Senha: gestor");
            System.out.println("-- Usuário consultor");
            System.out.println("- Login: Consultor");
            System.out.println("- Senha: consultor");
        }
    }

    private SizeEnum getRandomCompanySize() {
        SizeEnum[] sizes = SizeEnum.values();
        int randomIndex = faker.random().nextInt(sizes.length);
        return sizes[randomIndex];
    }

    private String getRandomSegment() {
        int randomIndex = faker.number().numberBetween(0, segments.length - 1);
        return segments[randomIndex];
    }

    private List<EvaluationRequest> generateEvaluationRequest() {
        List<EvaluationRequest> evaluationRequests = new ArrayList<>();

        // Supondo que há 10 questions para cada pillar (Ambiental, Social, Governamental)
        List<QuestionModel> enviornmentalQuestionsList = questionRepository.findByPillar(PillarEnum.Ambiental);
        List<QuestionModel> socialQuestionsList = questionRepository.findByPillar(PillarEnum.Social);
        List<QuestionModel> governmentQuestionsList = questionRepository.findByPillar(PillarEnum.Governamental);

        List<QuestionModel> selectedQuestions = getRandomQuestions(enviornmentalQuestionsList, 10);
        selectedQuestions.addAll(getRandomQuestions(socialQuestionsList, 10));
        selectedQuestions.addAll(getRandomQuestions(governmentQuestionsList, 10));

        for (QuestionModel question : selectedQuestions) {
            EvaluationRequest request = new EvaluationRequest();
            request.setQuestionId(question.getId());
            request.setQuestionPillar(question.getPillar());
            request.setUserAnswer(getAnswerProbability());
            evaluationRequests.add(request);
        }

        return evaluationRequests;
    }

    private List<QuestionModel> getRandomQuestions(List<QuestionModel> questions, int numberOfQuestions) {
        Random random = new Random();
        List<QuestionModel> randomQuestions = new ArrayList<>();
        List<Integer> selectedIndexes = new ArrayList<>();
    
        while (randomQuestions.size() < numberOfQuestions && selectedIndexes.size() < questions.size()) {
            int randomIndex = random.nextInt(questions.size());
            if (!selectedIndexes.contains(randomIndex)) {
                selectedIndexes.add(randomIndex);
                QuestionModel randomQuestion = questions.get(randomIndex);
                randomQuestions.add(randomQuestion);
            }
        }
        return randomQuestions;
    }
    
    private AnswersEnum getAnswerProbability() {
        int randomIndex = faker.random().nextInt(answerProbability.size());
        return answerProbability.get(randomIndex);
    }
    
    private String getRandomTradeName() {
        int randomIndex = faker.number().numberBetween(0, realCompanyNames.length - 1);
        String tradeName = realCompanyNames[randomIndex];
        realCompanyNames = ArrayUtils.removeElement(realCompanyNames, tradeName);
        return tradeName;
    }

    private List<AnswersEnum> generateAnswerPobability() {
        List<AnswersEnum> probabilities = new ArrayList<>();
        for (int i = 0; i < 45; i++) {
            probabilities.add(AnswersEnum.Conforme);
        }
        for (int i = 0; i < 35; i++) {
            probabilities.add(AnswersEnum.NaoConforme);
        }
        for (int i = 0; i < 20; i++) {
            probabilities.add(AnswersEnum.NaoSeAdequa);
        }
        return probabilities;
    }

    private static final String[] segments = {
        "Agricultura", "Automotivo", "Comércio", "Construção", "Educação", "Indústria", "Saúde", "Telecomunicações", "Transporte",
    };

    private static String[] realCompanyNames = {
        "Google", "Microsoft", "Apple", "Amazon", "Facebook", "IBM", "Intel", "Samsung", "Sony", "Coca-Cola",
        "PepsiCo", "Toyota", "Ford", "General Motors", "Volkswagen", "Siemens", "Procter & Gamble", "Johnson & Johnson",
        "Nestlé", "Unilever", "HP", "Dell", "Cisco", "Oracle", "Adobe", "Salesforce", "Tesla", "Netflix", "Uber",
        "Airbnb", "Spotify", "Lyft", "Snapchat", "Pinterest", "Twitter", "LinkedIn", "TikTok", "Zoom", "Slack",
        "Dropbox", "Shopify", "PayPal", "Square", "Stripe", "NVIDIA", "AMD", "Qualcomm", "Huawei", "Xiaomi",
        "Alibaba", "Tencent", "Baidu", "JD.com", "Berkshire Hathaway", "ExxonMobil", "Chevron", "Shell", "BP", "Total", "Aramco"
    };

    private final List<AnswersEnum> answerProbability = generateAnswerPobability();

    private static final String[] enviornmentalQuestions = {
        "A empresa possui uma política ambiental clara e documentada?",
        "A empresa possui metas de redução de emissões de carbono?",
        "A empresa gerencia adequadamente seus resíduos sólidos?",
        "A empresa utiliza fontes de energia renovável?",
        "Existem programas de eficiência energética implementados na empresa?",
        "A empresa realiza auditorias ambientais regulares?",
        "A empresa gerencia seu consumo de água de forma sustentável?",
        "A empresa possui políticas de reciclagem em vigor?",
        "A empresa lida adequadamente com a poluição do ar e da água gerada por suas operações?",
        "A empresa tem iniciativas para conservar a biodiversidade?",
        "A empresa oferece programas de educação ambiental para os funcionários?",
        "A empresa divulga seu desempenho ambiental em relatórios de sustentabilidade?",
        "A empresa investe em tecnologias limpas e sustentáveis?",
        "A empresa possui certificações ambientais, como ISO 14001?",
        "A empresa trabalha para minimizar o impacto ambiental ao longo de sua cadeia de suprimentos?",
        "A empresa avalia e mitiga o impacto ambiental de novos projetos?",
        "A empresa participa de iniciativas ou colaborações globais para a sustentabilidade ambiental?",
        "A empresa possui estratégias para lidar com as mudanças climáticas?",
        "A empresa incentiva práticas sustentáveis entre seus clientes e fornecedores?",
        "A empresa possui um compromisso com a restauração de ecossistemas afetados por suas operações?"
    };

    private static final String[] socialQuestions = {
        "A empresa possui políticas de diversidade e inclusão?",
        "A empresa compromete-se com a saúde e segurança dos funcionários?",
        "A empresa oferece oportunidades de desenvolvimento e capacitação para os empregados?",
        "A empresa engaja-se com as comunidades locais onde opera?",
        "A empresa promove igualdade de gênero no local de trabalho?",
        "A empresa possui práticas de remuneração e benefícios justas?",
        "A empresa possui políticas contra assédio e discriminação?",
        "A empresa contribui para a educação e formação profissional na comunidade?",
        "A empresa realiza auditorias sociais em sua cadeia de suprimentos?",
        "A empresa incentiva programas de voluntariado corporativo?",
        "A empresa apoia a saúde e o bem-estar dos funcionários além do local de trabalho?",
        "A empresa possui políticas contra trabalho infantil e trabalho forçado em sua cadeia de suprimentos?",
        "A empresa lida com questões de direitos humanos em suas operações globais?",
        "A empresa envolve os funcionários em decisões importantes que os afetam?",
        "A empresa possui uma alta taxa de retenção de funcionários?",
        "A empresa promove a participação dos funcionários em iniciativas de responsabilidade social?",
        "A empresa possui práticas transparentes de comunicação com as partes interessadas?",
        "A empresa mede e relata seu impacto social?",
        "A empresa apoia iniciativas culturais e esportivas nas comunidades onde atua?",
        "A empresa possui políticas de equilíbrio entre vida pessoal e profissional para os funcionários?"
    };

    private static final String[] governmentQuestions = {
        "A empresa possui uma estratégia clara de sustentabilidade ambiental?",
        "A empresa integra considerações ambientais em seu planejamento estratégico?",
        "A empresa realiza auditorias ambientais regulares?",
        "A empresa possui práticas de gestão de risco ambiental?",
        "A empresa possui uma política de governança ambiental documentada?",
        "A empresa gerencia a conformidade com as leis e regulamentos ambientais?",
        "A empresa divulga relatórios de sustentabilidade ambiental?",
        "A empresa possui práticas de transparência em relação ao seu impacto ambiental?",
        "A empresa possui um comitê de sustentabilidade ou um departamento dedicado ao meio ambiente?",
        "A empresa lida com a responsabilidade ambiental na cadeia de suprimentos?",
        "A empresa promove a participação dos stakeholders nas decisões ambientais?",
        "A empresa tem políticas para prevenir e mitigar a poluição ambiental?",
        "A empresa gerencia e minimiza seu impacto ambiental ao longo do ciclo de vida dos produtos?",
        "A empresa possui programas de treinamento ambiental para os funcionários?",
        "A empresa possui estratégias para reduzir suas emissões de carbono?",
        "A empresa investe em tecnologias limpas e sustentáveis?",
        "A empresa mede e gerencia seu consumo de recursos naturais?",
        "A empresa divulga seu desempenho ambiental em relatórios públicos?",
        "A empresa possui práticas de responsabilidade ambiental em relação às comunidades locais?",
        "A empresa tem um histórico de iniciativas e projetos de conservação ambiental?"
    };    
}

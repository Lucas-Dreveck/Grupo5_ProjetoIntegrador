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

import com.ambientese.grupo5.DTO.FormularioRequest;
import com.ambientese.grupo5.Model.*;
import com.ambientese.grupo5.Model.Enums.*;
import com.ambientese.grupo5.Repository.*;
import com.ambientese.grupo5.Services.FormulariosService.ProcessarFormularioService;
import com.github.javafaker.Faker;

@Component
public class InitialDataLoader implements CommandLineRunner {
    @Autowired
    private UsuarioRepository userRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private CargoRepository cargoRepository;

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private PerguntasRepository perguntasRepository;

    @Autowired
    private ProcessarFormularioService processarFormularioService;

    private final Faker faker = new Faker(new Locale("pt-BR"));

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        UsuarioModel rootUser = userRepository.findByLogin("root");

        if (rootUser == null) {
            int numberToGenerate = 60;

            // Popular tabela endereco e empresa
            for (int i = 0; i < numberToGenerate; i++) {
                EmpresaModel empresa = new EmpresaModel();
                empresa.setNomeFantasia(getRandomNomeFantasia());
                empresa.setNomeSolicitante(faker.name().fullName());
                String cellPhone = faker.phoneNumber().cellPhone();
                if (cellPhone.length() > 15) {
                    cellPhone = cellPhone.substring(0, 15);
                }
                empresa.setTelefoneSolicitante(cellPhone);
                empresa.setRazaoSocial(faker.company().name());
                empresa.setCnpj(faker.number().digits(14));
                empresa.setInscricaoSocial(faker.number().digits(20));
                empresa.setEmail(faker.internet().emailAddress());
                String telefone = faker.phoneNumber().phoneNumber();
                if (telefone.length() > 15) {
                    telefone = telefone.substring(0, 15);
                }
                empresa.setTelefoneEmpresas(telefone);
                empresa.setRamo(getRandomRamo());
                empresa.setPorteEmpresas(getRandomPorte());

                EnderecoModel endereco = new EnderecoModel();
                endereco.setCep(faker.address().zipCode());
                endereco.setNumero(faker.number().numberBetween(1, 1000));
                endereco.setLogradouro(faker.address().streetName());
                endereco.setComplemento(faker.address().secondaryAddress());
                endereco.setCidade(faker.address().cityName());
                endereco.setBairro(faker.address().streetName());
                endereco.setUF(faker.address().stateAbbr());

                empresa.setEndereco(endereco);

                empresaRepository.save(empresa);
            }

            // Popular tabela de cargos
            CargoModel cargoGestor = new CargoModel();
            cargoGestor.setDescricao("Gestor");
            cargoGestor = cargoRepository.save(cargoGestor);

            CargoModel cargoConsultor = new CargoModel();
            cargoConsultor.setDescricao("Consultor");
            cargoConsultor = cargoRepository.save(cargoConsultor);

            // Popular tabela de funcionarios
            for (int i = 0; i < numberToGenerate; i++) {
                UsuarioModel usuario = new UsuarioModel();
                usuario.setLogin(i == 0 ? "Gestor" : i == 1 ? "Consultor" : faker.name().username());
                usuario.setPassword(BCrypt.hashpw(i == 0 ? "gestor" : i == 1 ? "consultor" : faker.internet().password(), BCrypt.gensalt()));
                usuario.setIsAdmin(false);
                usuario = userRepository.save(usuario);

                FuncionarioModel funcionario = new FuncionarioModel();
                funcionario.setUsuario(usuario);
                funcionario.setNome(faker.name().fullName());
                funcionario.setCpf(faker.regexify("\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}"));
                funcionario.setEmail(i == 0 ? "Gestor@test.com" : i == 1 ? "Consultor@test.com" : faker.internet().emailAddress());

                // Converter Date para LocalDate
                Date birthday = faker.date().birthday();
                LocalDate localDate = birthday.toInstant()
                                            .atZone(ZoneId.systemDefault())
                                            .toLocalDate();
                funcionario.setDataNascimento(localDate);
                funcionario.setCargo(i == 0 ? cargoGestor : cargoConsultor);

                funcionarioRepository.save(funcionario);
            }

            // Popular tabela de perguntas
            for (EixoEnum eixo : EixoEnum.values()) {
                String[] perguntasArray;
                switch (eixo) {
                    case Ambiental:
                        perguntasArray = perguntasAmbiental;
                        break;
                    case Social:
                        perguntasArray = perguntasSocial;
                        break;
                    case Governamental:
                        perguntasArray = perguntasGovernamental;
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + eixo);
                }
                for (int i = 0; i < perguntasArray.length; i++) {
                    PerguntasModel pergunta = new PerguntasModel();
                    pergunta.setDescricao(perguntasArray[i]);
                    pergunta.setEixo(eixo);
                    perguntasRepository.save(pergunta);
                }
            }

            // Popular tabela de formulários utilizando o serviço ProcessarFormularioService
            List<EmpresaModel> empresas = empresaRepository.findAll();
            for (EmpresaModel empresa : empresas) {
                List<FormularioRequest> formularioRequests = generateFormularioRequests();
                processarFormularioService.criarFormularioCompleto(empresa.getId(), formularioRequests);
            }

            // Criação do usuário root
            UsuarioModel newUser = new UsuarioModel();
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

    private PorteEnum getRandomPorte() {
        PorteEnum[] portes = PorteEnum.values();
        int randomIndex = faker.random().nextInt(portes.length);
        return portes[randomIndex];
    }

    private String getRandomRamo() {
        int randomIndex = faker.number().numberBetween(0, ramos.length - 1);
        return ramos[randomIndex];
    }

    private List<FormularioRequest> generateFormularioRequests() {
        List<FormularioRequest> formularioRequests = new ArrayList<>();

        // Supondo que há 10 perguntas para cada eixo (Ambiental, Social, Governamental)
        List<PerguntasModel> perguntasAmbientalList = perguntasRepository.findByEixo(EixoEnum.Ambiental);
        List<PerguntasModel> perguntasSocialList = perguntasRepository.findByEixo(EixoEnum.Social);
        List<PerguntasModel> perguntasGovernamentalList = perguntasRepository.findByEixo(EixoEnum.Governamental);

        List<PerguntasModel> perguntasSelecionadas = getRandomQuestions(perguntasAmbientalList, 10);
        perguntasSelecionadas.addAll(getRandomQuestions(perguntasSocialList, 10));
        perguntasSelecionadas.addAll(getRandomQuestions(perguntasGovernamentalList, 10));

        for (PerguntasModel pergunta : perguntasSelecionadas) {
            FormularioRequest request = new FormularioRequest();
            request.setPerguntaId(pergunta.getId());
            request.setPerguntaEixo(pergunta.getEixo());
            request.setRespostaUsuario(getRespostaProbabilidade());
            formularioRequests.add(request);
        }

        return formularioRequests;
    }

    private List<PerguntasModel> getRandomQuestions(List<PerguntasModel> perguntas, int numberOfQuestions) {
        Random random = new Random();
        List<PerguntasModel> randomQuestions = new ArrayList<>();
        List<Integer> selectedIndexes = new ArrayList<>();
    
        while (randomQuestions.size() < numberOfQuestions && selectedIndexes.size() < perguntas.size()) {
            int randomIndex = random.nextInt(perguntas.size());
            if (!selectedIndexes.contains(randomIndex)) {
                selectedIndexes.add(randomIndex);
                PerguntasModel randomQuestion = perguntas.get(randomIndex);
                randomQuestions.add(randomQuestion);
            }
        }
        return randomQuestions;
    }
    
    private RespostasEnum getRespostaProbabilidade() {
        int randomIndex = faker.random().nextInt(respostaProbabilidades.size());
        return respostaProbabilidades.get(randomIndex);
    }
    
    private String getRandomNomeFantasia() {
        int randomIndex = faker.number().numberBetween(0, nomesReaisEmpresas.length - 1);
        String nomeFantasia = nomesReaisEmpresas[randomIndex];
        nomesReaisEmpresas = ArrayUtils.removeElement(nomesReaisEmpresas, nomeFantasia);
        return nomeFantasia;
    }

    private List<RespostasEnum> gerarRespostaProbabilidades() {
        List<RespostasEnum> probabilidades = new ArrayList<>();
        for (int i = 0; i < 45; i++) {
            probabilidades.add(RespostasEnum.Conforme);
        }
        for (int i = 0; i < 35; i++) {
            probabilidades.add(RespostasEnum.NaoConforme);
        }
        for (int i = 0; i < 20; i++) {
            probabilidades.add(RespostasEnum.NaoSeAdequa);
        }
        return probabilidades;
    }

    private static final String[] ramos = {
        "Agricultura", "Automotivo", "Comércio", "Construção", "Educação", "Indústria", "Saúde", "Telecomunicações", "Transporte",
    };

    private static String[] nomesReaisEmpresas = {
        "Google", "Microsoft", "Apple", "Amazon", "Facebook", "IBM", "Intel", "Samsung", "Sony", "Coca-Cola",
        "PepsiCo", "Toyota", "Ford", "General Motors", "Volkswagen", "Siemens", "Procter & Gamble", "Johnson & Johnson",
        "Nestlé", "Unilever", "HP", "Dell", "Cisco", "Oracle", "Adobe", "Salesforce", "Tesla", "Netflix", "Uber",
        "Airbnb", "Spotify", "Lyft", "Snapchat", "Pinterest", "Twitter", "LinkedIn", "TikTok", "Zoom", "Slack",
        "Dropbox", "Shopify", "PayPal", "Square", "Stripe", "NVIDIA", "AMD", "Qualcomm", "Huawei", "Xiaomi",
        "Alibaba", "Tencent", "Baidu", "JD.com", "Berkshire Hathaway", "ExxonMobil", "Chevron", "Shell", "BP", "Total", "Aramco"
    };

    private final List<RespostasEnum> respostaProbabilidades = gerarRespostaProbabilidades();

    private static final String[] perguntasAmbiental = {
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

    private static final String[] perguntasSocial = {
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

    private static final String[] perguntasGovernamental = {
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

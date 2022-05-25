package jsisfila;

import java.util.*;

public class Colegiado {
  private String codigo;
  private String nome;
  private Aluno alunoEmAtendimento;
  private Map<String, Aluno> alunos;
  private Deque<Aluno> fila;
  private Set<String> alunosAtendidos;
  private int atendimentos;

  public Colegiado() {
  }

  public Colegiado(String codigo, String nome) {
    this.setCodigo(codigo);
    this.setNome(nome);
    this.alunos = new HashMap<String, Aluno>();
    this.fila = new LinkedList<Aluno>();
    this.alunosAtendidos = new HashSet<String>();
    atendimentos = 0;
  }

  /**
   * Vincula aluno a este colegiado
   *
   * @param aluno
   * @throws IllegalStateException O aluno já está em outro colegiado.
   */
  public void insereAluno(Aluno aluno) throws IllegalStateException {
    if (aluno.hasColegiado())
      throw new IllegalStateException("O aluno ja esta em outro colegiado");

    alunos.put(aluno.getMatricula(), aluno);
    aluno.setColegiado(this);
  }

  /**
   * Retorna o aluno vinculado ao colegiado que possui a matrícula fornecida
   *
   * @param matricula Número de matrícula do aluno
   * @return Objeto aluno ou null se não encontrar
   */
  public Aluno findAluno(String matricula) {
    return alunos.get(matricula);
  }

  /**
   * @return true se o código do colegiado é válido,
   * ou false caso contrário
   */
  public boolean isValido() {
    return codigo.matches("[A-Z]+");
  }

  /**
   * Indica a situação de um aluno na fila de atendimento
   *
   * @param matricula Número de matrícula do aluno
   * @return
   */
  public SituacaoNaFila situacaoNaFila(String matricula) {
    int posicao = this.posicaoAluno(matricula);
    if (posicao == 0)
      return SituacaoNaFila.EM_ATENDIMENTO;
    else if (posicao > 0)
      return SituacaoNaFila.AGUARDANDO_ATENDIMENTO;
    else if (alunosAtendidos.contains(matricula))
      return SituacaoNaFila.JA_FOI_ATENDIDO;
    else
      return SituacaoNaFila.NAO_ESTA_NA_FILA;
  }

  /**
   * Adiciona aluno no final da fila
   *
   * @param matricula
   * @throws EnfileiraException       O aluno não puder ser adicionado à fila;
   *                                  use getSituacao() para saber a causa.
   * @throws IllegalArgumentException O número de matrícula é inválido
   * @throws NotFoundException        O aluno não está vinculado a um colegiado
   */
  public void enfileira(String matricula) throws EnfileiraException, IllegalArgumentException, NotFoundException {
    // numero de matricula invalido
    if (!Aluno.isMatriculaValida(matricula))
      throw new IllegalArgumentException("O numero de matricula e invalido");

    // encontra aluno correspondente, se vinculado
    Aluno aluno = this.findAluno(matricula);
    if (aluno == null)
      throw new NotFoundException("O aluno nao esta vinculado a um colegiado");

    // checa se aluno esta sendo atendido
    Aluno emAtendimento = this.alunoEmAtendimento();
    if (emAtendimento != null && emAtendimento.getMatricula().equals(matricula))
      throw new EnfileiraException(SituacaoNaFila.EM_ATENDIMENTO);

    // checa se o aluno esta na fila
    if (this.posicaoAluno(matricula) > 0)
      throw new EnfileiraException(SituacaoNaFila.AGUARDANDO_ATENDIMENTO);

    // adiciona aluno a fila
    fila.add(aluno);
  }

  /**
   * Indica se a fila está vazia, i.e., se todos os alunos que estavam
   * na fila já foram atendidos.
   *
   * @return
   */
  public boolean filaVazia() {
    return fila.isEmpty();
  }

  /**
   * Indica a posição de um aluno na fila de atendimento
   *
   * @param matricula Número de matrícula do aluno
   * @return 0, se o aluno está sendo atendido;
   * número maior que 0, se o aluno está aguardando atendimento;
   * -1 se o aluno já foi atendido ou nunca entrou na fila.
   */
  public int posicaoAluno(String matricula) {
    Aluno emAtendimento = this.alunoEmAtendimento();
    if (emAtendimento != null && emAtendimento.getMatricula().equals(matricula))
      return 0;

    int posicao = 1;
    for (Aluno aluno : fila) {
      if (aluno.getMatricula().equals(matricula))
        return posicao;
      posicao++;
    }

    return -1;
  }

  /**
   * Chama a próximo aluno da fila
   *
   * @return O aluno que era o próximo da fila ou
   * null se todos já haviam sido atendidos
   */
  public Aluno chamaProximo() {
    if (this.filaVazia())
      return null;

    Aluno aluno = fila.getFirst();
    fila.pop();
    alunoEmAtendimento = aluno;

    atendimentos++;
    alunosAtendidos.add(aluno.getMatricula());

    return aluno;
  }

  /**
   * @return O aluno que está sendo atendido ou
   * null se todos os alunos já foram atendidos.
   */
  public Aluno alunoEmAtendimento() {
    return alunoEmAtendimento;
  }

  /**
   * @return Quantidade de alunos atendidos ou em atendimento
   */
  public int totalAlunosAtendidos() {
    return alunosAtendidos.size();
  }

  /**
   * @return Quantidade de atendimentos realizados ou em andamento
   */
  public int totalAtendimentosRealizados() {
    return atendimentos;
  }

  /**
   * @return Quantidade de alunos na fila aguardando atendimento
   */
  public int totalAtendimentosFuturos() {
    return fila.size();
  }

  public String getCodigo() {
    return this.codigo;
  }

  public void setCodigo(String codigo) {
    this.codigo = codigo.toUpperCase();
  }

  public String getNome() {
    return this.nome;
  }

  public Map<String, Aluno> getAlunos() {
    return alunos;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public Aluno getProximoNaFila() {
    if (this.filaVazia())
      return null;

    return fila.getFirst();
  }
}

package jsisfila;

public class Aluno {
  private Colegiado colegiado;
  private String matricula;
  private String nome;

  public Aluno(String matricula, String nome) {
    this.setMatricula(matricula);
    this.setNome(nome);
  }

  public Colegiado getColegiado() {
    return colegiado;
  }

  public void setColegiado(Colegiado colegiado) {
    this.colegiado = colegiado;
  }

  public String getMatricula() {
    return matricula;
  }

  public void setMatricula(String matricula) {
    this.matricula = matricula;
  }

  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public boolean isValido() {
    return this.isMatriculaValida(this.matricula);
  }

  public boolean hasColegiado() {
    return colegiado != null;
  }

  public static boolean isMatriculaValida(String matricula) {
    return matricula.matches("[0-9]{9}");
  }
}

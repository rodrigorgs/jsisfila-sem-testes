package jsisfila;

public class NotFoundException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public NotFoundException(String message) {
    super(message);
  }

  @Override
  public String getMessage() {
    return "Não encontrado.";
  }
}

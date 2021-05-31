package gebd;

public class Uscita extends Operazione {
	
	private int idContoBeneficiario; // in caso di giroconto salvare tale valore
	
	public Uscita(int importo, String data, String originante, String beneficiario, int tipo,
			int codiceOperazioneLocale) {
		super(importo, data, originante, beneficiario, tipo, codiceOperazioneLocale);
	}
	
	public Uscita(int importo, String data, String originante, String beneficiario, int tipo, int codice,
			int codiceOperazioneLocale) {
		super(importo, data, originante, beneficiario, tipo, codiceOperazioneLocale);
		this.idContoBeneficiario = codice;
	}

	public int getIdContoBeneficiario() {
		return idContoBeneficiario;
	}
	
	

}

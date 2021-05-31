package gebd;

public final class Entrata extends Operazione {
	
	private int idContoOriginante;  // in caso di giroconto salvare tale valore
	
	public Entrata(int importo, String data, String originante, String beneficiario, int tipo, 
			int codiceOperazioneLocale) {
		super(importo, data, originante, beneficiario, tipo, codiceOperazioneLocale);
	}
	
	public Entrata(int importo, String data, String originante, String beneficiario, int tipo, int codice,
			int codiceOperazioneLocale) {
		super(importo, data, originante, beneficiario, tipo, codiceOperazioneLocale);
		this.idContoOriginante = codice;
		
	}

	public int getIdContoOriginante() {
		return idContoOriginante;
	}
	

}

package gebd;

public abstract class Operazione {
	
	private String data;
	private int importo;	
	private String originante;
	private String beneficiario;
	private int tipo;
	private int codiceOperazioneLocale;
	private int codiceOperazione;
	
	/*
	 * Tipo 1 = Bonifico
	 * Tipo 2 = Giroconto
	 */
	
	
	private static int numeroOperazioni = 0; 
	// codice operazione globale da prelevare con metodo getCodiceOperazione
	
	public Operazione(int importo, String data, String originante, String beneficiario, int tipo,
			int codiceOperazioneLocale) {
		this.importo = importo;
		this.data = data;
		this.originante = originante;
		this.beneficiario = beneficiario;
		this.tipo = tipo;
		this.codiceOperazioneLocale = codiceOperazioneLocale;
		codiceOperazione = numeroOperazioni++;
	}
	
	
	
	public String getData() {
		return data;
	}
	public int getImporto() {
		return importo;
	}
	public String getOriginante() {
		return originante;
	}
	public int getTipo() {
		return tipo;
	}

	public int getCodiceOperazione() {
		return codiceOperazione;
	}

	public String getBeneficiario() {
		return beneficiario;
	}

	public int getCodiceOperazioneLocale() {
		return codiceOperazioneLocale;
	}

	
	
	
	
		
	

}

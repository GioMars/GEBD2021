package gebd;

// import java.util.ArrayList;
import java.util.HashMap;

public class GestioneContiCorrenti {

	// protected ArrayList<ContoCorrente> contiCorrenti;           // ArrayList non effettivamente utilizzato
	protected HashMap<Integer, ContoCorrente> idToContoCorrente; 
	private static int numeroContoCorrente = 0;
		
	protected HashMap<Integer, Operazione> idToOperazione; 

	// Costruttore
	public GestioneContiCorrenti () {
		// contiCorrenti = new ArrayList<ContoCorrente>();
		idToContoCorrente = new HashMap<Integer, ContoCorrente>();
		idToOperazione = new HashMap<Integer, Operazione>();
	}
	
	public int creaContoCorrente(String nome, String cognome) {
		ContoCorrente c = new ContoCorrente(nome + " " + cognome, numeroContoCorrente);
		// contiCorrenti.add(c);
		idToContoCorrente.put(numeroContoCorrente, c);
		return numeroContoCorrente++;
	}
	
	public ContoCorrente getContoCorrente(int numeroContoCorrente) {
		return idToContoCorrente.get(numeroContoCorrente);
	}
	
	
	public void eliminaContoCorrente(int numeroContoCorrente) {
		ContoCorrente c = idToContoCorrente.get(numeroContoCorrente);
		idToContoCorrente.remove(numeroContoCorrente, c);
		// contiCorrenti.remove(c);
	}
	
	public int registraBonificoEntrata (int importo, int idContoCorrente, String ordinante,
			String data) {
		ContoCorrente x = idToContoCorrente.get(idContoCorrente);
		Operazione y = x.BonificoEntrata(importo, data, ordinante, x.getIntestatario());
		idToOperazione.put(y.getCodiceOperazione(), y);
		return y.getCodiceOperazione();
	}
	
	public int registraBonificoUscita (int importo, int idContoCorrente, String beneficiario,
			String data) {
		ContoCorrente x = idToContoCorrente.get(idContoCorrente);
		Operazione y = x.BonificoUscita(importo, data, x.getIntestatario(), beneficiario);
		idToOperazione.put(y.getCodiceOperazione(), y);
		return y.getCodiceOperazione();
	}
	
	
	// Problema metodologico, gestione non ottimale dei due codici restituiti
	public int[] giroconto (int importo, int idContoOrdinante, int idContoBenef,
			String data) {
		ContoCorrente a = idToContoCorrente.get(idContoOrdinante);
		ContoCorrente b = idToContoCorrente.get(idContoBenef);
		
		Operazione x = a.GirocontoUscita(importo,a.getIntestatario(), b.getIntestatario(), data, 
				idContoBenef);
		Operazione y = b.GirocontoEntrata(importo, a.getIntestatario(), b.getIntestatario(), data,
				idContoOrdinante);
		
		
		idToOperazione.put(x.getCodiceOperazione(), x);
		idToOperazione.put(y.getCodiceOperazione(), y);
		
		int[] codici = new int[2];
		codici[0] = x.getCodiceOperazione();
		codici[1] = y.getCodiceOperazione();
		
		return codici;
	}
	
	public void mostraOperazione(int codiceOperazione) {
		Operazione x = idToOperazione.get(codiceOperazione);
		String t;
		if (x.getTipo()==1) {
			t = "Bonifico";
		} else {
			t = "Giroconto";
		}
		if (x instanceof Entrata) {
			System.out.println("Operazione in entrata");
			System.out.println(t);
			System.out.println("Data: " + x.getData());
			System.out.println("Importo: " + x.getImporto());
			System.out.println("Ordinante: " + x.getOriginante());
			System.out.println("Beneficiario: " + x.getBeneficiario());
		}   else if (x instanceof Uscita) {
			System.out.println("Operazione in uscita");
			System.out.println(t);
			System.out.println("Data:" + x.getData());
			System.out.println("Importo: " + x.getImporto());
			System.out.println("Ordinante: " + x.getOriginante());
			System.out.println("Beneficiario: " + x.getBeneficiario());
			}
		}
	
	public void mostraContiCorrenti() {
		for (int key : idToContoCorrente.keySet()) {
			ContoCorrente x = idToContoCorrente.get(key);
		    System.out.println("ID Conto Corrente: " + key + ", Intestatario: " + x.getIntestatario());
		}
	}
	
	
	// Applicazione di TEST
	public static void main(String[] args) {
		GestioneContiCorrenti gs = new GestioneContiCorrenti();
		int idConto1 = gs.creaContoCorrente("Jeff","Bezof");
		int idConto2 = gs.creaContoCorrente("Elon","Musk");
		int idConto3 = gs.creaContoCorrente("Daniel","Zhang");
		
		int codiceProva = gs.registraBonificoEntrata(100000, idConto1, "Bill Gates", "2/3/2019");
		gs.registraBonificoEntrata(500000, idConto1, "Bill Gates", "5/3/2019");
		gs.registraBonificoUscita(100000, idConto1, "Mark Zuckerberg", "10/5/2020");
		
		gs.registraBonificoEntrata(65000, idConto2, "Bill Gates", "7/9/2020");
		int codiceProva2 = gs.registraBonificoUscita(43, idConto2, "Donald Trump", "23/3/2021");
		
		gs.registraBonificoEntrata(50000, idConto3, "Roman Abramovic", "6/3/2019");
		gs.registraBonificoEntrata(35, idConto3, "Bill Gates", "2/3/2020");
		gs.registraBonificoEntrata(150, idConto3, "Bill Gates", "2/7/2020");
		gs.registraBonificoEntrata(230, idConto3, "Michael Dell", "1/3/2021");
		
		int[] codiceProva3 = gs.giroconto(400, idConto2, idConto3, "23/3/2021");
		
		gs.getContoCorrente(idConto1).mostraOperazioni();
		int saldo = gs.getContoCorrente(idConto1).mostraSaldo();
		System.out.println("Saldo finale: " + saldo + "\n");
		
		gs.getContoCorrente(idConto2).mostraOperazioni();
		gs.getContoCorrente(idConto2).mostraSaldo();
		saldo = gs.getContoCorrente(idConto2).mostraSaldo();
		System.out.println("Saldo finale: " + saldo + "\n");
		
		gs.getContoCorrente(idConto3).mostraOperazioni();
		gs.getContoCorrente(idConto3).mostraSaldo();
	    saldo = gs.getContoCorrente(idConto3).mostraSaldo();
		System.out.println("Saldo finale: " + saldo + "\n");
		
		gs.getContoCorrente(idConto1).mostraOperazioniEntrata();
		
		System.out.println("\n");
		System.out.println("Singola operazione ID Globale");
		gs.mostraOperazione(codiceProva);
		
		System.out.println("\n");
		System.out.println("Singola operazione ID Globale");
		gs.mostraOperazione(codiceProva2);
		
		System.out.println("\n");
		System.out.println("Singola operazione ID Globale");
		gs.mostraOperazione(codiceProva3[0]);
		System.out.println("\n");
		gs.mostraOperazione(codiceProva3[1]);
		
		System.out.println("\n");
		System.out.println("Mostrare Conti Correnti");
		gs.mostraContiCorrenti();;
		
		gs.eliminaContoCorrente(idConto2);
		System.out.println("\n");
		System.out.println("Mostrare Conti Correnti dopo elinazione Elon Musk");
		gs.mostraContiCorrenti();
	}
	

}

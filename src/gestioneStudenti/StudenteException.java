package gestioneStudenti;
/*
 * Eccezione utilizzata per descrivere i casi in cui lo studente cercato non esiste con un 
 * certo codice identificativo
 * Di norma le EXCEPTION obbligano chi usa un certo codice a gestirle attraverso un blocco 
 * TRY/CATCH e attraverso un THROWS,
 * ciò potrebbe rendere il codice ingestibile
 * E' tuttavia possibile introdurre un diverso tipo di eccezione, meno vincolante e meno rigido,
 * chiamato RUNTIME EXCEPTION
 * una RUNTIME EXCEPTION non obbliga l'applicazione a gestirla ma sarebbe buona norma prevenirne l'insorgenza 
 * all'interno dei metodi che potrebbero generarla 
 * 
 * NELLA PRATICA 
 * Se non si indicasse RUNTIME l'applicazione non permetterebbe alcuna esecuzione (mostrando un segnale di errore) in presenza
 * della possibilità che un'eccezione sia sollevata; a meno che la suddetta eccezione non sia
 * adeguatamente gestita. Un'eccezione RUNTIME, seppur prevista, non obbliga l'utente a gestirla.
 */
public class StudenteException extends RuntimeException { 

}

package plp.enquanto.linguagem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public interface Linguagem {
	final Map<String, Integer> ambiente = new HashMap<>();
	final Scanner scanner = new Scanner(System.in);

	interface Bool {
		public boolean getValor();
	}

	interface Comando {
		public void execute();
	}

	interface Expressao {
		public int getValor();
	}

	abstract class ExpBin implements Expressao {
		protected Expressao esq;
		protected Expressao dir;

		public ExpBin(Expressao esq, Expressao dir) {
			this.esq = esq;
			this.dir = dir;
		}
	}

	class Programa {
		private List<Comando> comandos;
		public Programa(List<Comando> comandos) {
			this.comandos = comandos;
		}
		public void execute() {
			for (Comando comando : comandos) {
				comando.execute();
			}
		}
	}

	// Alterei o SE
	class Se implements Comando {
		private Bool condicao;
		private Comando entao;
		private Bool senaose;
		private Comando entaosenao;
		private Comando senao;

		public Se(Bool condicao, Comando entao, Bool senaose, Comando entaosenao, Comando senao) {
			this.condicao = condicao;
			this.entao = entao;
			this.senaose = senaose;
			this.entaosenao = entaosenao;
			this.senao = senao;
		}

		@Override
		public void execute() {
			if (condicao.getValor())
				entao.execute();
			else if (senaose.getValor())
				entaosenao.execute();
			else
				senao.execute();
		}
	}

	Skip skip = new Skip();
	class Skip implements Comando {
		@Override
		public void execute() {
		}
	}

	class Escreva implements Comando {
		private Expressao exp;

		public Escreva(Expressao exp) {
			this.exp = exp;
		}

		@Override
		public void execute() {
			System.out.println(exp.getValor());
		}
	}
	
	class Caso implements Comando {	
		private Expressao opcao;
		private Comando faca;
		
		public Caso(Expressao opcao, Comando faca){
			this.opcao = opcao;
			this.faca = faca;
		}
		
		@Override
		public void execute(){
			faca.execute();
		}
	}
	
	class Escolha implements Comando {		
		private Expressao variavel;
		private List<Caso> opcoes;
		private Comando padrao;
		
		public Escolha(Expressao variavel, Comando padrao){
			this.variavel = variavel;
			opcoes = new ArrayList<>();
			this.padrao = padrao;
		}
		
		public void AdicionarCaso(Caso caso){
			opcoes.add(caso);
		}
		
		@Override
		public void execute(){
			for(Caso caso: opcoes){
				if(caso.opcao.getValor() == variavel.getValor()){
					caso.execute();
					return;
				}
			}
			padrao.execute();
		}
	}

	class Enquanto implements Comando {
		private Bool condicao;
		private Comando faca;

		public Enquanto(Bool condicao, Comando faca) {
			this.condicao = condicao;
			this.faca = faca;
		}

		@Override
		public void execute() {
			while (condicao.getValor()) {
				faca.execute();
			}
		}
	}
	
	class Para implements Comando {
		private String id;
		private Expressao de;
		private Expressao ate;
		private Comando faca;
		
		public Para(String id, Expressao de, Expressao ate, Comando faca){
			this.id = id;
			this.de = de;
			this.ate = ate;
			this.faca = faca;
		}
		
		//para id de expressao ate expressao faca comando
		@Override
		public void execute() {
			for(int i = de.getValor(); i <= ate.getValor(); i++ ){
				ambiente.put(id, new Integer(i));
				faca.execute();
			}
		}
		
	}

	class Exiba implements Comando {
		public Exiba(String texto) {
			this.texto = texto;
		}

		private String texto;

		@Override
		public void execute() {
			System.out.println(texto);
		}
	}

	class Bloco implements Comando {
		private List<Comando> comandos;

		public Bloco(List<Comando> comandos) {
			this.comandos = comandos;
		}

		@Override
		public void execute() {
			for (Comando comando : comandos) {
				comando.execute();
			}
		}
	}

	class Atribuicao implements Comando {
		private String id;
		private Expressao exp;

		public Atribuicao(String id, Expressao exp) {
			this.id = id;
			this.exp = exp;
		}

		@Override
		public void execute() {
			ambiente.put(id, exp.getValor());
		}
	}

	class Inteiro implements Expressao {
		private int valor;

		public Inteiro(int valor) {
			this.valor = valor;
		}

		@Override
		public int getValor() {
			return valor;
		}
	}

	class Id implements Expressao {
		private String id;

		public Id(String id) {
			this.id = id;
		}

		@Override
		public int getValor() {
			final Integer v = ambiente.get(id);
			final int valor;
			if (v != null)
				valor = v;
			else
				valor = 0;

			return valor;
		}
	}

	Leia leia = new Leia();
	class Leia implements Expressao {
		@Override
		public int getValor() {
			return scanner.nextInt();
		}
	}

	class ExpSoma extends ExpBin {
		public ExpSoma(Expressao esq, Expressao dir) {
			super(esq, dir);
		}

		@Override
		public int getValor() {
			return esq.getValor() + dir.getValor();
		}
	}

	class ExpSub extends ExpBin {
		public ExpSub(Expressao esq, Expressao dir) {
			super(esq, dir);
		}

		@Override
		public int getValor() {
			return esq.getValor() - dir.getValor();
		}
	}
	
	class ExpMult extends ExpBin {
		public ExpMult(Expressao esq, Expressao dir) {
			super(esq, dir);
		}

		@Override
		public int getValor() {
			return esq.getValor() * dir.getValor();
		}
	}
	
	class ExpDiv extends ExpBin{
		public ExpDiv (Expressao esq, Expressao dir) {
			super(esq, dir);
		}
		
		@Override
		public int getValor() {
			return esq.getValor() / dir.getValor();
		}
		
	}
	
	class ExpExpon extends ExpBin{
		public ExpExpon(Expressao esq, Expressao dir) {
			super(esq, dir);
		}
		
		@Override
		public int getValor() {
			return (int)Math.pow(esq.getValor(), dir.getValor());
		}
		
	}

	class Booleano implements Bool {
		private boolean valor;

		public Booleano(boolean valor) {
			this.valor = valor;
		}

		@Override
		public boolean getValor() {
			return valor;
		}
	}

	abstract class ExpRel implements Bool {
		protected Expressao esq;
		protected Expressao dir;

		public ExpRel(Expressao esq, Expressao dir) {
			this.esq = esq;
			this.dir = dir;
		}
	}

	public class ExpIgual extends ExpRel {

		public ExpIgual(Expressao esq, Expressao dir) {
			super(esq, dir);
		}

		@Override
		public boolean getValor() {
			return esq.getValor() == dir.getValor();
		}

	}

	public class ExpMenorIgual extends ExpRel {
		public ExpMenorIgual(Expressao esq, Expressao dir) {
			super(esq, dir);
		}

		@Override
		public boolean getValor() {
			return esq.getValor() <= dir.getValor();
		}
	}
	
	public class ExpMaiorIgual extends ExpRel {
		public ExpMaiorIgual (Expressao esq, Expressao dir) {
			super(esq, dir);
		}
		
		@Override
		public boolean getValor() {
			return esq.getValor() >= dir.getValor();
		}
		
	}
	
	public class ExpDiferente extends ExpRel {
		public ExpDiferente(Expressao esq, Expressao dir) {
			super(esq, dir);
		}

		@Override
		public boolean getValor() {
			return esq.getValor() != dir.getValor();
		}
	}

	public class NaoLogico implements Bool {
		private Bool b;

		public NaoLogico(Bool b) {
			this.b = b;
		}

		@Override
		public boolean getValor() {
			return !b.getValor();
		}
	}

	public class ELogico implements Bool {
		private Bool esq;
		private Bool dir;

		public ELogico(Bool esq, Bool dir) {
			this.esq = esq;
			this.dir = dir;
		}

		@Override
		public boolean getValor() {
			return esq.getValor() && dir.getValor();
		}
	}
	
	public class OuLogico implements Bool {
		private Bool esq;
		private Bool dir;
		
		public OuLogico(Bool esq, Bool dir) {
			this.esq = esq;
			this.dir = dir;
		}
		
		@Override
		public boolean getValor() {
			return esq.getValor() || dir.getValor();
		}		
	}
	
	public class XorLogico implements Bool {
		private Bool esq;
		private Bool dir;
		
		public XorLogico(Bool esq, Bool dir) {
			this.esq = esq;
			this.dir = dir;
		}
		
		@Override
		public boolean getValor() {
			return (!esq.getValor() && dir.getValor()) || (esq.getValor() && !dir.getValor());
		}
		
	}
}


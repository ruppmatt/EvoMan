package evoman.test;

import evoman.evo.EvoPool;
import evoman.evo.SimulationManager;
import evoman.evo.VariationManager;

public class TestSimternet {
	
	public static void main(String[] argv){
		EvoPool root = new EvoPool("root");
		EvoPool asp = new EvoPool("asp",root);
		EvoPool nsp = new EvoPool("nsp", root);
		EvoPool nsp_pricing = new EvoPool("PricingStrategy",nsp);
		EvoPool nsp_investment = new EvoPool("InvestmentStrategy",nsp);
		EvoPool nsp_interconnect = new EvoPool("InterconnectStrategy",nsp);
		EvoPool nsp_edgebackbone = new EvoPool("EdgeBackboneStrategy",nsp);
		

		//Connect EvoPools
		root.addEvoPool(nsp);
		root.addEvoPool(asp);
		nsp.addEvoPool(nsp_pricing);
		nsp.addEvoPool(nsp_investment);
		nsp.addEvoPool(nsp_interconnect);
		nsp.addEvoPool(nsp_edgebackbone);
		asp.addEvoPool(new EvoPool("tree1",asp));
		asp.addEvoPool(new EvoPool("tree2",asp));
		

		
		//Add VariationManagers
		nsp_pricing.setVM(new VariationManager("VM-Pricing",nsp));
		nsp_investment.setVM(new VariationManager("VM-Investment",nsp));
		nsp_interconnect.setVM(new VariationManager("VM-Interconnect",nsp));
		nsp_edgebackbone.setVM(new VariationManager("VM-EdgeBackbone",nsp));
		((EvoPool) root.resolveName("asp.tree1")).setVM(new VariationManager("GP-Default",asp));
		((EvoPool) root.resolveName("asp.tree2")).setVM(new VariationManager("GP-Default",asp));
		
		//Add SimulationManager
		root.setSM(new SimulationManager("SimternetManager", root));
		
		root.init();
		System.out.println(root.toString());
		root.finish();
	}
}

package test;

import org.junit.*;

import evoman.bindings.ecj.*;
import evoman.evo.sm.*;
import evoman.evo.structs.*;
import evoman.evo.vm.*;

public class TestGP {
	
	@Test
	public void test(){
		EvoPool root = new EvoPool("root");
		EvoPool asp = new EvoPool("asp",root);
		EvoPool nsp = new EvoPool("nsp", root);
		EvoPool nsp_pricing = new EvoPool("PricingStrategy",nsp);
		EvoPool nsp_investment = new EvoPool("InvestmentStrategy",nsp);
		EvoPool nsp_interconnect = new EvoPool("InterconnectStrategy",nsp);
		EvoPool nsp_edgebackbone = new EvoPool("EdgeBackboneStrategy",nsp);
		GPVariationManager nsp_pricing_vm = new GPVariationManager("ASP1",nsp_pricing);
		GPOperator test = new GPOperator("BooleanOpAnd");
		test.setPath("evoman.bindings.ecj.operations.BooleanOpAnd");
		test.setNumChildren(2);
		nsp_pricing_vm.addOperator(test);
		
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
		nsp_pricing.setVM(nsp_pricing_vm);
		nsp_investment.setVM(new VariationManager("VM-Investment",nsp));
		nsp_interconnect.setVM(new VariationManager("VM-Interconnect",nsp));
		nsp_edgebackbone.setVM(new VariationManager("VM-EdgeBackbone",nsp));
		((EvoPool) root.resolveName("asp.tree1")).setVM(new VariationManager("GP-Default",asp));
		((EvoPool) root.resolveName("asp.tree2")).setVM(new VariationManager("GP-Default",asp));
		
		
		//Add SimulationManager
		root.setSM(new SimulationManager("SimternetManager", root));
		
		System.err.println("About to initialize.");
		root.init();
		System.err.println("Initialization successful.");
		System.out.println(root.toString());
		root.finish();
	}
}

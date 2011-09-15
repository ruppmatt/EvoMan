package evoman.test;

import evoman.evo.EvoPool;

public class TestStructure {
	
	public static void main(String[] args){
		EvoPool root = new EvoPool("root");
		
		EvoPool asp = new EvoPool("asp",root);
		EvoPool nsp = new EvoPool("nsp", root);
		
		//Add EvoPools to root
		root.addEvoPool(nsp);
		root.addEvoPool(asp);
		
		//Add EvoPool using addEvoPool
		asp.addEvoPool(new EvoPool("tree1",nsp));
		
		//Add EvoPool by creating it first and then adding it
		EvoPool nsp_tree2 = new EvoPool("tree2",nsp);
		asp.addEvoPool(nsp_tree2);
		
		//Add EvoPool by moving it from a parent-less state
		EvoPool nsp_tree3 = new EvoPool("tree3",null);
		nsp_tree3.moveTo( (EvoPool) root.resolveName("asp") );
		
		//Add an EvoPool by moving it from another parent
		asp.addEvoPool(new EvoPool("tree4",asp));
		EvoPool tree = (EvoPool) root.resolveName("asp.tree4");
		tree.moveTo(nsp);
		
		//Add another EvoPool so there's not an empty asp subpool
		asp.addEvoPool(new EvoPool("tree5",asp));
		
		asp.moveTo((EvoPool) root.resolveName("nsp"));
		System.out.println(root.toString());
		
		asp.moveTo(root);
		System.out.println(root.toString());
		
	}

}

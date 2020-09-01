package model.dao;

import java.util.List;

import model.entities.Department;
import model.entities.Seller;

//INTERFACE DAO PARA SELLER
public interface SellerDao {

	// INSERIR NO BANCO DE DADOS O OBJETO
	void insert(Seller obj);
	
	// ATUALIZAR NO BANCO DE DADOS O OBJETO
	void update(Seller obj);
	
	// DELETAR NO BANCO DE DADOS O OBJETO
	void deleteById(Integer id);
	
	// PEGAR O ID E CONSULTAR NO BANCO DE DADOS
	Seller findById(Integer id);
	
	// EXIBIR LISTA DE DEPARTAMENTOS
	List<Seller> findAll(); 
	
	// EXIBIR LISTA POR DEPARTAMENTO
	List<Seller> findByDepartment(Department department);
		
}

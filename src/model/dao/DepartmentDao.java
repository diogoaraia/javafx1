package model.dao;

import java.util.List;

import model.entities.Department;

// INTERFACE DAO PARA DEPARTMENT
public interface DepartmentDao {

	// INSERIR NO BANCO DE DADOS O OBJETO
	void insert(Department obj);
	
	// ATUALIZAR NO BANCO DE DADOS O OBJETO
	void update(Department obj);
	
	// DELETAR NO BANCO DE DADOS O OBJETO
	void deleteById(Integer id);
	
	// PEGAR O ID E CONSULTAR NO BANCO DE DADOS
	Department findById(Integer id);
	
	// EXIBIR LISTA DE DEPARTAMENTOS
	List<Department> findAll(); 
		
}

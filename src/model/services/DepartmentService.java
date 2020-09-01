package model.services;

// MOCK
//import java.util.ArrayList;

import java.util.List;
import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

public class DepartmentService {
	
	// DECLARACAO DE DEPENDENCIA
	private DepartmentDao dao = DaoFactory.createDepartmentDao();
	
	// MÉTODO PARA EXIBIT LISTA DE DEPARTMENT
	public List<Department> findAll(){
		// MOCK = DADOS DE MENTIRA PARA TESTES
		// List<Department> list = new ArrayList<>();
		// list.add(new Department(1, "Books"));
		// list.add(new Department(2, "Computer"));
		// list.add(new Department(3, "Electronics"));
		// return list;	
		return dao.findAll();		
	}
	
	// MÉTODO PARA INSERIR O DEPARTAMENTO O ID SENDO NULO OU
	// E ATUALIZAR O DEPARTAMENTO SE  O ID JA EXISTIR
	public void saveOrUpdate(Department obj) {
		// INSERINDO DEPARTAMENTO
		if (obj.getId() == null) {
			dao.insert(obj);
		}
		// DEPARTAMENTO ESTA SENDO ATUALIZADO
		else {
			dao.update(obj);
		}
	}
	
	// METODO PARA REMOVER O DEPARTAMENTO
	public void remove(Department obj) {
		dao.deleteById(obj.getId());
	}
}

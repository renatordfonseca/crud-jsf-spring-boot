package br.com.renatofonseca.projetoweb.controller;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.PrimeFaces;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.renatofonseca.projetoweb.model.PessoaSalario;
import br.com.renatofonseca.projetoweb.repository.PessoaSalarioRepository;

@Named
@ViewScoped
@Controller
@RequestMapping("pessoaSalario")
public class PessoaSalarioController {

	@Autowired
	private PessoaSalarioRepository pessoaSalarioRepository;
	
	private List<PessoaSalario> pessoa;
	
    private int currentPage = 0;
    private int pageSize = 10;
    
    private String nomeCompleto;

    @PostConstruct
    public void init() {
        loadPessoas();
    }

    public void nextPage() {
        currentPage++;
        loadPessoas();
    }

    public void previousPage() {
        if (currentPage > 0) {
            currentPage--;
            loadPessoas();
        }
    }

    private void loadPessoas() {
        Pageable pageable = PageRequest.of(currentPage, pageSize);
        Page<PessoaSalario> pessoaPage = (Page<PessoaSalario>) pessoaSalarioRepository.findAll(pageable);
        pessoa = pessoaPage.getContent();
    }
    
    @RequestMapping("/ataualizarSalario")
    public void atualizarSalario() {

    	List<Object[]> resultados = (List<Object[]>) pessoaSalarioRepository.buscarParaAtualizarSalario();
    	
    	for (Object[] resultado : resultados) {

    		int pessoaId = (int) resultado[0];
    	    String pessoa = (String) resultado[1];
    	    String valorVencimento = resultado[5].toString();
    	    
    	    List<PessoaSalario> existePessoaSalario = pessoaSalarioRepository.findById(pessoaId); 
    	    
    	    if(existePessoaSalario.isEmpty()) {
    	    	pessoaSalarioRepository.savePessoaSalario(pessoa, valorVencimento, pessoaId);
    	    }
    	    
    	    pessoaSalarioRepository.updatePessoaSalario(pessoa, valorVencimento, pessoaId);
    	}
    	PrimeFaces.current().ajax().update("tabelaSalarios");
    }

    
    @RequestMapping("/cargaInicialSalario")
    public void cargaInicialSalario() {

    	List<Object[]> resultados = (List<Object[]>) pessoaSalarioRepository.buscarParaAtualizarSalario();
    	
    	List<PessoaSalario> existePessoaSalario = (List<PessoaSalario>) pessoaSalarioRepository.findAll(); 
    	
    	if(existePessoaSalario.isEmpty()) {
    		
    		for (Object[] resultado : resultados) {
    			
    			int pessoaId = (int) resultado[0];
    			String pessoa = (String) resultado[1];
    			String valorVencimento = resultado[5].toString();
    			
    			pessoaSalarioRepository.savePessoaSalario(pessoa, valorVencimento, pessoaId);
    		}    	
    	}
    	
    }
    
	public void buscarPorNome() {
		pessoa = pessoaSalarioRepository.findByNome(nomeCompleto);
    }
    
   
	public List<PessoaSalario> getPessoa() {
		return pessoa;
	}

	public void setPessoa(List<PessoaSalario> pessoa) {
		this.pessoa = pessoa;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public String getNomeCompleto() {
		return nomeCompleto;
	}

	public void setNomeCompleto(String nomeCompleto) {
		this.nomeCompleto = nomeCompleto;
	}
	
}

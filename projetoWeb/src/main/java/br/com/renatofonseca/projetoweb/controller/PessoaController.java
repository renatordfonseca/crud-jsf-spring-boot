package br.com.renatofonseca.projetoweb.controller;

import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.renatofonseca.projetoweb.model.Pessoa;
import br.com.renatofonseca.projetoweb.repository.PessoaRepository;
import br.com.renatofonseca.projetoweb.repository.PessoaSalarioRepository;


@Named
@SessionScoped
@Controller
@RequestMapping("pessoa")
public class PessoaController {

	@Autowired
	private PessoaRepository pessoaRepository;
	
	@Autowired
	private PessoaSalarioRepository pessoaSalarioRepository;
	
	private List<Pessoa> pessoa;
	
	private String nomeCompleto;
	private String dataNascimento;
	
	private List<Pessoa> resultados;
	
	private Pessoa pessoaSelecionada;
	
	private int cargo;

    private int currentPage = 0;
    private int pageSize = 10;
    
    ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();

    @PostConstruct
    public void init() {
        loadPessoa();
    }

    public void nextPage() {
        currentPage++;
        loadPessoa();
    }

    public void previousPage() {
        if (currentPage > 0) {
            currentPage--;
            loadPessoa();
        }
    }

    private void loadPessoa() {
        Pageable pageable = PageRequest.of(currentPage, pageSize);
        Page<Pessoa> pessoaPage = (Page<Pessoa>) pessoaRepository.findAll(pageable);
        pessoa = pessoaPage.getContent();
    }

	
	public void deletar(Pessoa pessoa){
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "EXCLUIDO", "Excluido com sucesso!"));
		pessoaSalarioRepository.deletePessoaSalario(pessoa.getId());
		pessoaRepository.deleteById(pessoa.getId());
	}
	
	@RequestMapping("/pessoa")
	public void salvar() {
		
		pessoaRepository.savePessoa(nomeCompleto, cargo);
		
		nomeCompleto = null;
	}
	
	public void buscarPorNome() {
        resultados = pessoaRepository.findByNomeCompleto(nomeCompleto);
    }
	
	 public void editarPessoa(Pessoa pessoa) {
	        try {
	        	pessoaSelecionada = pessoa;
	            externalContext.redirect("editarPessoa.xhtml");
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	 
	 public void salvarEdicao() {
		 try {
			 pessoaRepository.editPessoa(pessoaSelecionada.getNome(), cargo, pessoaSelecionada.getId());
			 externalContext.redirect("gerenciamentoPessoa.xhtml");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 }
	 
	public Pessoa getPessoaSelecionada() {
		return pessoaSelecionada;
	}

	public void setPessoaSelecionada(Pessoa pessoaSelecionada) {
		this.pessoaSelecionada = pessoaSelecionada;
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

	public List<Pessoa> getPessoa() {
		return pessoa;
	}

	public void setPessoa(List<Pessoa> pessoa) {
		this.pessoa = pessoa;
	}

	public String getNomeCompleto() {
		return nomeCompleto;
	}

	public void setNomeCompleto(String nomeCompleto) {
		this.nomeCompleto = nomeCompleto;
	}

	public List<Pessoa> getResultados() {
		return resultados;
	}

	public void setResultados(List<Pessoa> resultados) {
		this.resultados = resultados;
	}
	public String getDataNascimento() {
		return dataNascimento;
	}
	
	public void setDataNascimento(String dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public int getCargo() {
		return cargo;
	}

	public void setCargo(int cargo) {
		this.cargo = cargo;
	}
}

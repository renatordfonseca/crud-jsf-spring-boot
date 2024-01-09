package br.com.renatofonseca.projetoweb.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.renatofonseca.projetoweb.model.Pessoa;
import br.com.renatofonseca.projetoweb.model.PessoaSalario;

@Repository
public interface PessoaRepository extends CrudRepository<Pessoa, Long> {
	
	@Query(value = "select pessoa from public.pessoa p "
			+ "inner join public.cargo c on c.id = p.cargo_id  "
			+ "inner join public.cargo_vencimento cv  on cv.id = c.id "
			+ "inner join public.vencimentos v  on v.id = cv.vencimento_id "
			+ "where p.id = 1 ", nativeQuery = true)
	public PessoaSalario buscarPorGeral();

	
	@Query(value = "SELECT p FROM Pessoa p WHERE LOWER(p.nome) LIKE LOWER(CONCAT('%', :nome, '%'))")
	List<Pessoa> findByNomeCompleto(@Param("nome") String nome);

	public Page<Pessoa> findAll(Pageable pageable);

	@Modifying
	@Transactional
	@Query(value = "INSERT INTO public.pessoa "
			+ " (nome, cargo_id) "
			+ " VALUES(:nome, :cargo_id)", nativeQuery = true)
	void savePessoa(@Param("nome") String nome, @Param("cargo_id") int cargo_id);
	
	@Modifying
	@Transactional
	@Query(value = "UPDATE public.pessoa "
			+ "	SET nome = :nome, cargo_id = :cargo_id "
			+ "	WHERE id = :idPessoa ", nativeQuery = true)
	void editPessoa(@Param("nome") String nome, @Param("cargo_id") int cargo_id, @Param("idPessoa") long idPessoa);
	
}

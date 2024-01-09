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

import br.com.renatofonseca.projetoweb.model.PessoaSalario;

@Repository
public interface PessoaSalarioRepository extends CrudRepository<PessoaSalario, Long> {

	public Page<PessoaSalario> findAll(Pageable pageable);
	
	@Query(value = "select p.id, p.nome, "
			+ "c.id as id_cargo, c.nome as nome_cargo, "
			+ "cv.vencimento_id as vencimento_id, v.valor as v_valor  "
			+ "from public.pessoa p "
			+ "inner join public.cargo c on c.id = p.cargo_id  "
			+ "inner join public.cargo_vencimento cv  on cv.id = c.id "
			+ "inner join public.vencimentos v  on v.id = cv.vencimento_id ", nativeQuery = true)
	public List<Object[]> buscarParaAtualizarSalario();
	
	@Modifying
	@Transactional
	@Query(value = "INSERT INTO public.pessoa_salario "
			+ " (nome, salario, pessoa_id) "
			+ " VALUES(:nome, :salario, :pessoaId)", nativeQuery = true)
	void savePessoaSalario(@Param("nome") String nome, @Param("salario") String salario, @Param("pessoaId") int pessoaId);
	
	
	@Modifying
	@Transactional
    @Query("UPDATE PessoaSalario SET nome = :nome, salario = :salario WHERE pessoa_id = :pessoaId")
    void updatePessoaSalario(@Param("nome") String nome, @Param("salario") String salario, @Param("pessoaId") int pessoaId);
	
	@Modifying
	@Transactional
    @Query(value = "DELETE FROM public.pessoa_salario WHERE pessoa_id = :pessoaId", nativeQuery = true)
    void deletePessoaSalario(@Param("pessoaId") long pessoaId);
	
	@Query(value = "SELECT p FROM PessoaSalario p WHERE LOWER(p.nome) LIKE LOWER(CONCAT('%', :nome, '%'))")
	List<PessoaSalario> findByNome(@Param("nome") String nome);
	
	@Query(value = "select * from pessoa_salario ps where ps.pessoa_id = :pessoaId", nativeQuery = true)
	List<PessoaSalario> findById(@Param("pessoaId") int pessoaId);

}

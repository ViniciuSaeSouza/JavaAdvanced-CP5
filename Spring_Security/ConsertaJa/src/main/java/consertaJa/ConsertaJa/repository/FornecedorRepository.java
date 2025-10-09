package consertaJa.ConsertaJa.repository;

import consertaJa.ConsertaJa.model.Fornecedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FornecedorRepository extends JpaRepository<Fornecedor, Long> {

    @Query("SELECT f FROM Fornecedor f WHERE LOWER(f.nome) LIKE LOWER(CONCAT('%', :nome, '%'))")
    List<Fornecedor> searchByNome(@Param("nome") String nome);

    @Query("SELECT f FROM Fornecedor f WHERE LOWER(f.email) LIKE LOWER(CONCAT('%', :email, '%'))")
    List<Fornecedor> searchByEmail(@Param("email") String email);
    Fornecedor findByCnpjIgnoreCase(String cnpj);
}

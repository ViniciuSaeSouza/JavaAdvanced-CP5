package consertaJa.ConsertaJa.repository;

import consertaJa.ConsertaJa.model.Ferramenta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FerramentaRepository extends JpaRepository<Ferramenta, Long> {
    List<Ferramenta> findByEstoqueId(Long estoqueId);
}

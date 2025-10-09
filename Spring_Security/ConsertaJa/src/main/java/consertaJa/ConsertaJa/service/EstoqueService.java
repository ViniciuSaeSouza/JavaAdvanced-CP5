package consertaJa.ConsertaJa.service;

import consertaJa.ConsertaJa.dto.EstoqueRequestDto;
import consertaJa.ConsertaJa.dto.EstoqueResponseDto;
import consertaJa.ConsertaJa.exception.IdNaoEncontradoException;
import consertaJa.ConsertaJa.model.Estoque;
import consertaJa.ConsertaJa.model.Ferramenta;
import consertaJa.ConsertaJa.repository.EstoqueRepository;
import consertaJa.ConsertaJa.repository.FerramentaRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EstoqueService {

    @Autowired
    private EstoqueRepository repository;

    @Autowired
    private FerramentaRepository ferramentaRepository;

    @Transactional
    public Estoque save(EstoqueRequestDto dto) {
        Estoque estoque = new Estoque();
        estoque.setCapacidade(dto.capacidade());
        estoque.setLocalArmazenamento(dto.localArmazenamento());
        return repository.save(estoque);
    }

    @Transactional(readOnly = true)
    public List<EstoqueResponseDto> findAll() {
        return repository.findAll()
                .stream()
                .map(EstoqueResponseDto::new)
                .toList();
    }

    @Transactional()
    public EstoqueResponseDto findById(Long id) throws IdNaoEncontradoException {
        return repository.findById(id)
                .map(EstoqueResponseDto::new)
                .orElseThrow(() -> new IdNaoEncontradoException("Estoque não encontrado com o ID: " + id));
    }

    @Transactional
    public EstoqueResponseDto update(Long id, EstoqueRequestDto dto) throws IdNaoEncontradoException {
        Estoque estoque = repository.findById(id)
                .orElseThrow(() -> new IdNaoEncontradoException("Estoque não encontrado com o ID: " + id));

        estoque.setCapacidade(dto.capacidade());
        estoque.setLocalArmazenamento(dto.localArmazenamento());

        Estoque estoqueAtualizado = repository.save(estoque);
        return new EstoqueResponseDto(estoqueAtualizado);
    }

    @Transactional
    public void delete(Long id) throws IdNaoEncontradoException {
        if (!repository.existsById(id)) {
            throw new IdNaoEncontradoException("Estoque não encontrado com o ID: " + id);
        }
        repository.deleteById(id);
    }


    @Transactional
    public EstoqueResponseDto patch(Long id, int novaCapacidade) throws IdNaoEncontradoException {
        Estoque estoque = repository.findById(id)
                .orElseThrow(() -> new IdNaoEncontradoException("Estoque não encontrado com o ID: " + id));

        if (novaCapacidade < 0) {
            throw new RuntimeException("A capacidade deve ser um valor não-negativo.");
        }

        int qtdArmazenada = ferramentaRepository.findByEstoqueId(id)
                .stream()
                .mapToInt(Ferramenta::getQuantidade)
                .sum();

        if (qtdArmazenada > novaCapacidade) {
            String local = estoque.getLocalArmazenamento() == null ? "Não informado" : estoque.getLocalArmazenamento();
            String msg = String.format(
                    "Não é possível reduzir a capacidade do estoque em '%s'. Capacidade proposta: %d, quantidade já armazenada: %d.",
                    local, novaCapacidade, qtdArmazenada
            );
            throw new RuntimeException(msg);
        }

        estoque.setCapacidade(novaCapacidade);

        repository.save(estoque);

        return new EstoqueResponseDto(estoque);
    }
}

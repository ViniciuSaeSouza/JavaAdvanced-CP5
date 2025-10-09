package consertaJa.ConsertaJa.service;

import consertaJa.ConsertaJa.dto.FerramentaRequestDto;
import consertaJa.ConsertaJa.dto.FerramentaResponseDto;
import consertaJa.ConsertaJa.exception.IdNaoEncontradoException;
import consertaJa.ConsertaJa.exception.QuantidadeInvalidaException;
import consertaJa.ConsertaJa.exception.ValorNegativoException;
import consertaJa.ConsertaJa.model.Estoque;
import consertaJa.ConsertaJa.model.Ferramenta;
import consertaJa.ConsertaJa.model.Fornecedor;
import consertaJa.ConsertaJa.repository.EstoqueRepository;
import consertaJa.ConsertaJa.repository.FerramentaRepository;
import consertaJa.ConsertaJa.repository.FornecedorRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FerramentaService {

    @Autowired
    private FerramentaRepository repository;

    @Autowired
    private EstoqueRepository estoqueRepository;

    @Autowired
    private FornecedorRepository fornecedorRepository;

    @Transactional
    public Ferramenta save(FerramentaRequestDto dto) throws IdNaoEncontradoException {
        Fornecedor fornecedor = null;
        Estoque estoque = null;

        if (dto.fornecedorId() != null) {
            fornecedor = fornecedorRepository.findById(dto.fornecedorId())
                    .orElseThrow(() -> new IdNaoEncontradoException("Fornecedor não encontrado"));
        }

        if (dto.estoqueId() != null) {
            estoque = estoqueRepository.findById(dto.estoqueId())
                    .orElseThrow(() -> new IdNaoEncontradoException("Estoque não encontrado"));

            int used = repository.findByEstoqueId(estoque.getId())
                    .stream()
                    .mapToInt(Ferramenta::getQuantidade)
                    .sum();

            if (used + dto.quantidade() > estoque.getCapacidade()) {
                String msg = String.format(
                        "Ultrapassou a capacidade do estoque no local '%s'. Capacidade: %d, já usado: %d, tentando adicionar: %d.",
                        estoque.getLocalArmazenamento() == null ? "Não informado" : estoque.getLocalArmazenamento(),
                        estoque.getCapacidade(),
                        used,
                        dto.quantidade()
                );
                throw new RuntimeException(msg);
            }
        }

        return repository.save(new Ferramenta(dto, fornecedor, estoque));
    }

    @Transactional(readOnly = true)
    public List<FerramentaResponseDto> findAll(){
        return repository.findAll().stream().map(FerramentaResponseDto::new).toList();
    }

    @Transactional(readOnly = true)
    public FerramentaResponseDto findById(Long id){
        Optional<Ferramenta> ferramenta = repository.findById(id);

        if (ferramenta.isPresent()){
            Ferramenta f = ferramenta.get();
            return new FerramentaResponseDto(f);
        } else {
            return null;
        }
    }

    @Transactional
    public FerramentaResponseDto update(Long id, FerramentaResponseDto dto) throws IdNaoEncontradoException {
        Fornecedor fornecedor = null;

        if (dto.fornecedorId() != null) {
            fornecedor = fornecedorRepository.findById(dto.fornecedorId())
                    .orElseThrow(() -> new IdNaoEncontradoException("Fornecedor não encontrado"));
        }


        Ferramenta ferramenta = repository.findById(id)
                .orElseThrow(() -> new IdNaoEncontradoException("Ferramenta não encontrada"));

        int novoQuantidade = dto.quantidade();

        Estoque estoqueDestino = null;
        if (dto.estoqueId() != null) {
            estoqueDestino = estoqueRepository.findById(dto.estoqueId())
                    .orElseThrow(() -> new IdNaoEncontradoException("Estoque não encontrado"));
        } else {
            estoqueDestino = ferramenta.getEstoque();
        }

        if (estoqueDestino != null) {
            int usadoNoDestino = repository.findByEstoqueId(estoqueDestino.getId())
                    .stream()
                    .filter(f -> !f.getId().equals(ferramenta.getId()))
                    .mapToInt(Ferramenta::getQuantidade)
                    .sum();

            if (usadoNoDestino + novoQuantidade > estoqueDestino.getCapacidade()) {
                String msg = String.format(
                        "Ultrapassou a capacidade do estoque no local '%s'. Capacidade: %d, já usado: %d, tentando adicionar: %d.",
                        estoqueDestino.getLocalArmazenamento() == null ? "Não informado" : estoqueDestino.getLocalArmazenamento(),
                        estoqueDestino.getCapacidade(),
                        usadoNoDestino,
                        novoQuantidade
                );
                throw new RuntimeException(msg);
            }

            ferramenta.setEstoque(estoqueDestino);
        }

        ferramenta.setNome(dto.nome());
        ferramenta.setTipo(dto.tipo());
        ferramenta.setClassificacao(dto.classificacao());
        ferramenta.setTamanho(dto.tamanho());
        ferramenta.setPreco(dto.preco());
        ferramenta.setQuantidade(novoQuantidade);
        ferramenta.setFornecedor(fornecedor);

        Ferramenta atualizado = repository.save(ferramenta);
        return new FerramentaResponseDto(atualizado);
    }


    @Transactional
    public FerramentaResponseDto atualizarPreco(Long id, double novoPreco) throws IdNaoEncontradoException {
        Ferramenta ferramenta = repository.findById(id)
                .orElseThrow(() -> new IdNaoEncontradoException("Ferramenta não encontrada"));

        ferramenta.setPreco(novoPreco);

        repository.save(ferramenta);

        return new FerramentaResponseDto(ferramenta);
    }

    @Transactional
    public FerramentaResponseDto adicionarQuantidade(Long id, int quantidadeSoma) throws IdNaoEncontradoException, ValorNegativoException {
        Ferramenta ferramenta = repository.findById(id)
                .orElseThrow(() -> new IdNaoEncontradoException("Ferramenta não encontrada"));

        validaQuantidade(id, ferramenta, quantidadeSoma);

        int novaQuantidade = ferramenta.adicionarQuantidade(quantidadeSoma);

        ferramenta.setQuantidade(novaQuantidade);

        repository.save(ferramenta);

        return new FerramentaResponseDto(ferramenta);
    }

    @Transactional
    public FerramentaResponseDto retirarQuantidade(Long id, int quantidadeSub) throws IdNaoEncontradoException, ValorNegativoException, QuantidadeInvalidaException {
        Ferramenta ferramenta = repository.findById(id)
                .orElseThrow(() -> new IdNaoEncontradoException("Ferramenta não encontrada"));

        int novaQuantidade = ferramenta.retirarQuantidade(quantidadeSub);

        ferramenta.setQuantidade(novaQuantidade);

        repository.save(ferramenta);

        return new FerramentaResponseDto(ferramenta);
    }

    @Transactional
    public void delete(Long id) throws IdNaoEncontradoException {
        Ferramenta ferramenta = repository.findById(id)
                .orElseThrow(() -> new IdNaoEncontradoException("Ferramenta não encontrada"));

        repository.delete(ferramenta);
    }

    public void validaQuantidade(Long id, Ferramenta ferramenta, int quantidade){
        int capacidade = ferramenta.getEstoque().getCapacidade();

        int usedInTarget = repository.findByEstoqueId(ferramenta.getEstoque().getId())
                .stream()
                .filter(f -> !f.getId().equals(id))
                .mapToInt(Ferramenta::getQuantidade)
                .sum();

        int totalDepoisDoPatch = usedInTarget + (ferramenta.getQuantidade() + quantidade);

        if (totalDepoisDoPatch > capacidade) {
            String msg = String.format(
                    "Ultrapassou a capacidade do estoque no local '%s'. Capacidade: %d, já usado: %d, tentando ajustar/transferir: %d.",
                    ferramenta.getEstoque().getLocalArmazenamento() == null ? "Não informado" : ferramenta.getEstoque().getLocalArmazenamento(),
                    capacidade,
                    usedInTarget + ferramenta.getQuantidade(),
                    quantidade
            );
            throw new RuntimeException(msg);
        }
    }
}

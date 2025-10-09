package consertaJa.ConsertaJa.service;

import consertaJa.ConsertaJa.dto.FornecedorRequestDto;
import consertaJa.ConsertaJa.dto.FornecedorResponseDto;
import consertaJa.ConsertaJa.exception.IdNaoEncontradoException;
import consertaJa.ConsertaJa.model.Fornecedor;
import consertaJa.ConsertaJa.repository.FornecedorRepository;
import consertaJa.ConsertaJa.utils.FormatUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FornecedorService {

    @Autowired
    private FornecedorRepository repository;

    @Transactional
    public Fornecedor save(FornecedorRequestDto dto) {

        Fornecedor fornecedor = new Fornecedor(dto);

        if (dto.cnpj() != null) {
            fornecedor.setCnpj(FormatUtils.formatCnpj(dto.cnpj()));
        }
        if (dto.telefone() != null) {
            fornecedor.setTelefone(FormatUtils.formatTelefone(dto.telefone()));
        }

        return repository.save(fornecedor);
    }

    @Transactional
    public List<FornecedorResponseDto> listAll() {
        return repository.findAll().stream()
                .map(FornecedorResponseDto::new)
                .toList();
    }

    @Transactional
    public List<FornecedorResponseDto> listByName(String nome) {
        return repository.searchByNome(nome)
                .stream()
                .map(FornecedorResponseDto::new)
                .toList();
    }

    @Transactional
    public FornecedorResponseDto listByCnpj(String cnpj) {
        String cnpjFormatado = FormatUtils.formatCnpj(cnpj);
        Fornecedor fornecedor = repository.findByCnpjIgnoreCase(cnpjFormatado);
        return new FornecedorResponseDto(fornecedor);
    }

    @Transactional
    public List<FornecedorResponseDto> listByEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Por favor insira um email válido");
        }
        return repository.searchByEmail(email)
                .stream()
                .map(FornecedorResponseDto::new)
                .toList();
    }

    @Transactional
    public FornecedorResponseDto findById(Long id) {
        Optional<Fornecedor> fornecedor = repository.findById(id);
        return fornecedor.map(FornecedorResponseDto::new).orElse(null);
    }

    @Transactional
    public FornecedorResponseDto update(Long id, FornecedorResponseDto dto) throws IdNaoEncontradoException {
        Fornecedor f = repository.findById(id)
                .orElseThrow(() -> new IdNaoEncontradoException("Fornecedor não encontrado"));

        f.setNome(dto.nome());
        f.setEmail(dto.email());

        if (dto.cnpj() != null) {
            f.setCnpj(FormatUtils.formatCnpj(dto.cnpj()));
        }

        if (dto.telefone() != null && !dto.telefone().isBlank()) {
            f.setTelefone(FormatUtils.formatTelefone(dto.telefone()));
        }

        Fornecedor atualizado = repository.save(f);
        return new FornecedorResponseDto(atualizado);
    }

    @Transactional
    public FornecedorResponseDto patchEmail(Long id, String email) throws IdNaoEncontradoException {
        Fornecedor fornecedor = repository.findById(id)
                .orElseThrow(() -> new IdNaoEncontradoException(
                        "Não foi possível encontrar o fornecedor com esse id"));

        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Por favor insira um email válido");
        }

        fornecedor.setEmail(email);
        repository.save(fornecedor);

        return new FornecedorResponseDto(fornecedor);
    }

    @Transactional
    public FornecedorResponseDto patchTelefone(Long id, String telefone) throws IdNaoEncontradoException {
        Fornecedor fornecedor = repository.findById(id)
                .orElseThrow(() -> new IdNaoEncontradoException(
                        "Não foi possível encontrar o fornecedor com esse id"));

        if (telefone == null || telefone.isBlank()) {
            throw new IllegalArgumentException("Telefone não pode ser nulo ou vazio");
        }

        // Formata antes de salvar
        String telefoneFormatado = FormatUtils.formatTelefone(telefone);
        fornecedor.setTelefone(telefoneFormatado);

        repository.save(fornecedor);
        return new FornecedorResponseDto(fornecedor);
    }

    @Transactional
    public void delete(Long id) throws IdNaoEncontradoException {
        Fornecedor fornecedor = repository.findById(id)
                .orElseThrow(() -> new IdNaoEncontradoException(
                        "Não foi possível encontrar o fornecedor com este id"));

        repository.delete(fornecedor);
    }
}

package com.junior.locadora.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junior.locadora.domain.Cadastro;
import com.junior.locadora.dto.CadastroDTO;
import com.junior.locadora.repository.CadastroRepository;
import com.junior.locadora.services.exception.ObjectNotFoundException;

@Service
public class CadastroService {

    private final CadastroRepository repo;

    @Autowired
    public CadastroService(CadastroRepository repo) {
        this.repo = repo;
    }

    public List<Cadastro> findAll() {
        return repo.findAll();
    }

    public Cadastro criarCadastro(String nome, String descricao, double duracao, int idademin) {
        // Validar os campos de entrada (opcional)
        if (nome == null || descricao == null || duracao <= 0 || idademin < 0) {
            throw new IllegalArgumentException("Campos de entrada inválidos.");
        }

        LocalDateTime dataHoraCadastro = LocalDateTime.now(); // Obtém a data e hora atuais

        Cadastro cadastro = new Cadastro(null, nome, descricao, duracao, idademin, dataHoraCadastro);
        return repo.save(cadastro);
    }

    public Cadastro findById(String id) {
        Optional<Cadastro> cadastroOptional = repo.findById(id);
        if (!cadastroOptional.isPresent()) {
            throw new ObjectNotFoundException("Cadastro não encontrado");
        }
        return cadastroOptional.get();
    }

    public Cadastro insert(Cadastro obj) {
        return repo.insert(obj);
    }

    public void delete(String id) {
        Cadastro cadastro = findById(id); // Primeiro, encontre o objeto pelo ID
        repo.delete(cadastro); // Agora, delete o objeto encontrado
    }

    public Cadastro update(Cadastro obj) {
        Optional<Cadastro> optionalCadastro = repo.findById(obj.getId());

        if (optionalCadastro.isEmpty()) {
            throw new ObjectNotFoundException("Cadastro não encontrado");
        }

        Cadastro existingCadastro = optionalCadastro.get();

        updateData(existingCadastro, obj);

        return repo.save(existingCadastro);
    }

    private void updateData(Cadastro newObj, Cadastro obj) {
        newObj.setNome(obj.getNome());
        newObj.setDescricao(obj.getDescricao());
        newObj.setDuracao(obj.getDuracao());
        newObj.setIdademin(obj.getIdademin());
        newObj.setDataHoraCadastro(obj.getDataHoraCadastro());
    }

    public Cadastro fromDTO(CadastroDTO objDto) {
        return new Cadastro(objDto.getId(), objDto.getNome(), objDto.getDescricao(), objDto.getDuracao(),
                objDto.getIdademin(), objDto.getDataHoraCadastro());
    }
}

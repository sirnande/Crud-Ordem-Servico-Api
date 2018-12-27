package com.para.crudos.api.services;

import com.para.crudos.api.auditoria.Auditoria;
import com.para.crudos.api.dtos.TecnicoDTO;
import com.para.crudos.api.exceptions.ValidacaoException;
import com.para.crudos.api.model.Tecnico;
import com.para.crudos.api.repositories.TecnicoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TecnicoService{

    private static  final Logger log = LoggerFactory.getLogger(TecnicoService.class);

    @Autowired
    private TecnicoRepository tecnicoRepository;

    @Autowired
    private ConversionService conversionService;

    private Auditoria<TecnicoDTO> auditoria = new Auditoria<>();

    public TecnicoDTO salvar(TecnicoDTO tecnicoDto){
        log.info("Salvar um novo tecnico: {}", tecnicoDto.toString());
        validarDadosExistentes(tecnicoDto);

        tecnicoDto = this.conversionService.convert(
                this.tecnicoRepository.save(this.conversionService.convert(tecnicoDto, Tecnico.class)),

                TecnicoDTO.class
        );
        auditoria.post(new TecnicoDTO(), tecnicoDto, "Tecnico");
        return tecnicoDto;
    }

    public TecnicoDTO atualizar(TecnicoDTO tecnicoDto){
        log.info("Atualizando tecnico: {}", tecnicoDto.toString());
        TecnicoDTO tecnicoDTOAnt = buscarPorId(tecnicoDto.getId());
        TecnicoDTO tecnicoDTONovo = this.conversionService.convert(
                this.tecnicoRepository.save(
                        this.conversionService.convert(tecnicoDto, Tecnico.class)
                ),

                TecnicoDTO.class
        );
        auditoria.post(tecnicoDTOAnt, tecnicoDTONovo, "Tecnico");

        return tecnicoDTONovo;
    }


    public TecnicoDTO buscarPorId(Long id) {
        log.info("Buscando um tecnico pelo id: {}", id);
        Tecnico tecnico = this.tecnicoRepository.findById(id)
                .orElseThrow(() -> new ValidacaoException("Tecnico não encotrado para o id: " + id));

        return this.conversionService.convert(
                tecnico,

                TecnicoDTO.class
        );
    }

    public TecnicoDTO buscarPorNome(String nome) {
        log.info("Buscar tecnico por nome: {}", nome);
        return this.conversionService.convert(
                        Optional.ofNullable(this.tecnicoRepository.findByNome(nome))
                            .orElseThrow(() -> new ValidacaoException("Tecnico  não encontrado para o nome: "+ nome)),

                        TecnicoDTO.class
                );
    }

    public String remover(Long id) {
        log.info("Deletando um novo tecnico por id: {}", id);
        TecnicoDTO tecnicoDTO = buscarPorId(id);
        this.tecnicoRepository.deleteById(id);
        auditoria.post(tecnicoDTO, new TecnicoDTO(), "Tecnico");
        return "Tecnico excluido com sucesso...";
    }

    private void validarDadosExistentes(TecnicoDTO tecnicoDTO){
        if (this.tecnicoRepository.findByNome(tecnicoDTO.getNome()) != null){
            throw new ValidacaoException("Nome já existe");
        }
    }
}

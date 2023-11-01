package com.vacinas.ap1.controller;

        import com.vacinas.ap1.entity.Mensagem;
        import com.vacinas.ap1.entity.Vacina;
        import com.vacinas.ap1.exceptions.VacinaNotFoundException;
        import com.vacinas.ap1.exceptions.VacinaNotInsertExeption;
        import com.vacinas.ap1.service.ServiceVacina;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.http.HttpStatus;
        import org.springframework.http.MediaType;
        import org.springframework.http.ResponseEntity;
        import org.springframework.validation.annotation.Validated;
        import org.springframework.web.bind.annotation.*;

        import javax.validation.Valid;
        import java.util.List;
        import java.util.Map;

@RestController
@Validated
public class VacinaController {
    @Autowired
    private ServiceVacina serviceVacina;

    //Método para adicionar vacina

    @PostMapping("/vacinas/cadastrar")
    public ResponseEntity inserir(@RequestBody  @Valid Vacina novaVacina) {
        if(serviceVacina.existeVacina(novaVacina)){
            throw new VacinaNotInsertExeption("Vacina existente na base!");
        }
        serviceVacina.inserir(novaVacina);
        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(novaVacina);
    }

    //Método para listar as vacinas
    @GetMapping("/vacinas")
    public ResponseEntity<List<Vacina>> obterTodos() {
        List<Vacina> vacinas = serviceVacina.obterTodos();
        if (vacinas.isEmpty()) {
            throw new VacinaNotFoundException("Vacina(s) não encontrada(s)!");
        }
        return ResponseEntity.status(200)
                .contentType(MediaType.APPLICATION_JSON)
                .body(vacinas);
    }
    //Metodo para pegar vacinas por id
    @GetMapping("/vacinas/{id}")
    public ResponseEntity<Vacina> obterPorId(@PathVariable String id) {
        if (serviceVacina.obterPorId(id) == null) {
            throw new VacinaNotFoundException("Vacina(s) não encontrada(s)!");
        }
        return ResponseEntity.status(200)
                .contentType(MediaType.APPLICATION_JSON)
                .body(serviceVacina.obterPorId(id));
    }
    //Metodo para editar vacina pelo seu body
    @PutMapping("/vacinas/editar")
    public ResponseEntity<Vacina> editarVacina(@RequestBody  @Valid Vacina vacinaEditada){
        if (serviceVacina.obterPorId(vacinaEditada.getId()) == null) {
            throw new VacinaNotFoundException("Vacina(s) não encontrada(s)!");
        }
        serviceVacina.editar(vacinaEditada);
        return ResponseEntity.status(200)
                .contentType(MediaType.APPLICATION_JSON)
                .body(serviceVacina.obterPorId(vacinaEditada.getId()));
    }
    //Metodo que edita a vacina por id e requer um body
    @PutMapping("/vacinas/{id}/editar")
    public ResponseEntity<Vacina> editarVacinaPorId(@PathVariable String id, @RequestBody  @Valid Vacina vacinaEditada){
        if (serviceVacina.obterPorId(id) == null) {
            throw new VacinaNotFoundException("Vacina(s) não encontrada(s)!");
        }
        vacinaEditada.setId(id);
        serviceVacina.editar(vacinaEditada);
        return ResponseEntity.status(200)
                .contentType(MediaType.APPLICATION_JSON)
                .body(serviceVacina.obterPorId(id));
    }

    //Metodo para edição parcial por id e requer um body
    @PatchMapping("/vacinas/{id}/editar")
    public ResponseEntity<Vacina> atualizarParcialPorId(@PathVariable String id, @RequestBody Map<String, Object> updates) {
        Vacina vacina = serviceVacina.obterPorId(id);

        if (vacina == null) {
            throw new VacinaNotFoundException("Vacina(s) não encontrada(s)");
        }

        for (Map.Entry<String, Object> entry : updates.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            switch (key) {
                case "nome":
                    vacina.setNome((String) value);
                    break;
                case "fabricante":
                    vacina.setFabricante((String) value);
                    break;
                case "lote":
                    vacina.setLote((String) value);
                    break;
                case "data_validade":
                    vacina.setData_validade((String) value);
                    break;
                case "numero_de_doses":
                    vacina.setNumero_de_doses((Integer) value);
                    break;
                case "intervalo_doses":
                    vacina.setIntervalo_doses((Integer) value);
                    break;
            }
        }

        serviceVacina.editar(vacina);

        return ResponseEntity.ok(vacina);
    }

    @PatchMapping("/vacinas/editar")
    public ResponseEntity<Vacina> atualizarParcialmenteVacina(@RequestBody  @Valid Vacina vacinaEditada) {

        if (vacinaEditada == null) {
            throw new VacinaNotFoundException("Vacina(s) não encontrada(s)");
        }

        vacinaEditada.setNome(vacinaEditada.getNome());
        vacinaEditada.setFabricante(vacinaEditada.getFabricante());
        vacinaEditada.setData_validade(vacinaEditada.getData_validade());
        vacinaEditada.setNumero_de_doses(vacinaEditada.getNumero_de_doses());
        vacinaEditada.setIntervalo_doses(vacinaEditada.getIntervalo_doses());

        serviceVacina.editar(vacinaEditada);

        return ResponseEntity.ok(vacinaEditada);
    }


    //Metodo que exclui vacinas por id
    @DeleteMapping("/vacinas/{id}/excluir")
    public ResponseEntity<Mensagem> deletarPorId(@PathVariable String id) {
        if (serviceVacina.obterPorId(id) == null) {
            throw new VacinaNotFoundException("Vacina(s) não encontrada(s)!");
        }

        serviceVacina.deletarPorId(id);
        return ResponseEntity.status(200)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new Mensagem("Vacina excluída com sucesso!"));
    }
    //Metodo que exclui todos os b
    @DeleteMapping("/vacinas/excluir")
    public ResponseEntity<Mensagem> deletarTodos() {
        if (serviceVacina.obterTodos().isEmpty()) {
            throw new VacinaNotFoundException("Vacina(s) não encontrada(s)!");
        }
        serviceVacina.deletarTodos();
        return ResponseEntity.status(200)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new Mensagem("Vacina excluída com sucesso!"));
    }
}




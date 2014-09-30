package br.com.arrasaamiga

enum StatusVenda {

    AguardandoPagamento("Aguardando pagamento"),
    EmAnalise("Em análise"),
    PagamentoRecebido("Paga"),
    Disponivel("Disponível"),
    EmDisputa("Em disputa"),
    Devolvida("Devolvida"),
    Cancelada("Cancelada"),
    Entregue("Entregue")

    String descricao

    StatusVenda(String desc) {
        this.descricao = desc
    }

    public String toString() {
        return this.descricao
    }

}

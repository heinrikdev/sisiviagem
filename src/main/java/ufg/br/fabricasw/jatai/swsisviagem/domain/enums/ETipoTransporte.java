package ufg.br.fabricasw.jatai.swsisviagem.domain.enums;

import ufg.br.fabricasw.jatai.swsisviagem.domain.solicitacao.Solicitacao;

import java.util.ArrayList;
import java.util.List;

public enum ETipoTransporte {

    AEREO {
        @Override
        public Integer id() {
            return 0;
        }

        @Override
        public String toString() {
            return "Aéreo";
        }
    },
    VEICULO_OFICIAL {
        @Override
        public Integer id() {
            return 1;
        }

        @Override
        public String toString() {
            return "Veículo oficial";
        }
    },
    VEICULO_PROPRIO {
        @Override
        public Integer id() {
            return 2;
        }

        @Override
        public String toString() {
            return "Veículo próprio";
        }
    },
    ONIBUS {
        @Override
        public Integer id() {
            return 3;
        }

        @Override
        public String toString() {
            return "Veículo Rodoviário";
        }
    };

    public abstract Integer id();

    @Override
    public abstract String toString();

    /**
     * @param solicitacoes
     * @return
     */
    public static List<ETipoTransporte> tipoTransporteToSolicitacoes(List<Solicitacao> solicitacoes) {

        List<ETipoTransporte> lista = new ArrayList<>();

        for (Solicitacao e : solicitacoes) {

            switch (e.getTipoSolicitacao()) {
                case DIARIA:
                    //Se for diaria
                    
                    lista.add(ONIBUS);
                    lista.add(VEICULO_PROPRIO);
                    break;
                case PASSAGEM:
                    //Se for passagem
                    
                    lista.add(AEREO);
                    break;
                case TRANSPORTE:
                    //Se fir Trasnposte
                    
                    lista.add(VEICULO_OFICIAL);
                    break;
                default:
                    //Se esquecer de algum ele da o throw e fica facil de encontrar o "problema" =)
                    throw new IllegalArgumentException("O Tipo nao foi configurado (Configure): " + e.toString());
            }
        }

        return lista;
    }
}

package ufg.br.fabricasw.jatai.swsisviagem.repository.unidades;

/**
 *
 * @author Ronaldo N. de Sousa
 */
public class QueriesUnidades {
    
    public static final String SELECT_COUNT_RESULTS_BY_PROPOSTO =
    "SELECT COUNT(DISTINCT r.id) FROM requisicao r                                          " +
    "    INNER JOIN requisicao_solicitacoes rs   ON rs.requisicao_id = r.id                 " +
    "    INNER JOIN solicitacao s                ON s.id             = rs.solicitacoes_id   " +
    "    INNER JOIN pessoa p                     ON p.id             = r.proposto_id        " +
    "WHERE                                                                                  " +
    "    r.unidade_id        = :unidade_id            AND                                   " +
    "    p.nome              LIKE %:proposto%       AND                                     " +
    "    r.estado_requisicao = 'SUBMETIDA'   AND                                            " +
    "    (                                                                                  " +
    "        (                                                                              " +
    "            s.tipo_solicitacao  = 'DIARIA'          OR                                 " +
    "            s.tipo_solicitacao  = 'PASSAGEM'                                           " +
    "        )                                           AND                                " +
    "        s.estado_solicitacao    != 'PREENCHENDO'                                       " +
    "    )                                                                                  " +
    "    AND NOT EXISTS (                                                                   " +
    "        SELECT s.id FROM solicitacao s                                                 " +
    "            INNER JOIN requisicao_solicitacoes rs       ON                             " +
    "                s.id = rs.solicitacoes_id                                              " +
    "        WHERE                                                                          " +
    "            rs.requisicao_id    = r.id                  AND                            " +
    "            s.tipo_solicitacao  = 'TRANSPORTE'          AND                            " +
    "            (                                                                          " +
    "                s.estado_solicitacao = 'RECUSADA'       OR                             " +
    "                s.estado_solicitacao = 'PREENCHENDO'    OR                             " +
    "                s.estado_solicitacao = 'SUBMETIDA'                                     " +
    "            )                                                                          " +
    "    )                                                                                  " +
    "ORDER BY YEAR(r.data_requisicao) DESC, r.data_requisicao ASC                           ";
    
    public static final String SELECT_COUNT_RESULTS_BY_REQUISICAO_ID =
    "SELECT COUNT(DISTINCT r.id) FROM requisicao r                                          " +
    "    INNER JOIN requisicao_solicitacoes rs   ON rs.requisicao_id = r.id                 " +
    "    INNER JOIN solicitacao s                ON s.id             = rs.solicitacoes_id   " +
    "WHERE                                                                                  " +
    "    r.unidade_id        = :unidade_id            AND                                   " +
    "    r.estado_requisicao = 'SUBMETIDA'   AND                                            " +
    "    (                                                                                  " +
    "        (                                                                              " +
    "            s.tipo_solicitacao  = 'DIARIA'          OR                                 " +
    "            s.tipo_solicitacao  = 'PASSAGEM'                                           " +
    "        )                                           AND                                " +
    "        s.estado_solicitacao    != 'PREENCHENDO'                                       " +
    "    )                                                                                  " +
    "    AND NOT EXISTS (                                                                   " +
    "        SELECT s.id FROM solicitacao s                                                 " +
    "            INNER JOIN requisicao_solicitacoes rs       ON                             " +
    "                s.id = rs.solicitacoes_id                                              " +
    "        WHERE                                                                          " +
    "            rs.requisicao_id    = r.id                  AND                            " +
    "            s.tipo_solicitacao  = 'TRANSPORTE'          AND                            " +
    "            (                                                                          " +
    "                s.estado_solicitacao = 'RECUSADA'       OR                             " +
    "                s.estado_solicitacao = 'PREENCHENDO'    OR                             " +
    "                s.estado_solicitacao = 'SUBMETIDA'                                     " +
    "            )                                                                          " +
    "    )                                                                                  " +
    "    AND r.id = :requisicao_id                                                          " +
    "ORDER BY YEAR(r.data_requisicao) DESC, r.data_requisicao ASC                           ";
    
    public static final String SELECT_COUNT_RESULTS_BY_STATUS = 
    "SELECT COUNT(DISTINCT r.id) FROM requisicao r                                          " +
    "    INNER JOIN requisicao_solicitacoes rs   ON rs.requisicao_id = r.id                 " +
    "    INNER JOIN solicitacao s                ON s.id             = rs.solicitacoes_id   " +
    "WHERE                                                                                  " +
    "    r.unidade_id        = :unidade_id            AND                                   " +
    "    r.estado_requisicao = 'SUBMETIDA'   AND                                            " +
    "    (                                                                                  " +
    "        (                                                                              " +
    "            s.tipo_solicitacao  = 'DIARIA'          OR                                 " +
    "            s.tipo_solicitacao  = 'PASSAGEM'                                           " +
    "        )                                           AND                                " +
    "        s.estado_solicitacao    LIKE :estado_solicitacao                               " +
    "    )                                                                                  " +
    "    AND NOT EXISTS (                                                                   " +
    "        SELECT s.id FROM solicitacao s                                                 " +
    "            INNER JOIN requisicao_solicitacoes rs       ON                             " +
    "                s.id = rs.solicitacoes_id                                              " +
    "        WHERE                                                                          " +
    "            rs.requisicao_id    = r.id                  AND                            " +
    "            s.tipo_solicitacao  = 'TRANSPORTE'          AND                            " +
    "            (                                                                          " +
    "                s.estado_solicitacao = 'RECUSADA'       OR                             " +
    "                s.estado_solicitacao = 'PREENCHENDO'    OR                             " +
    "                s.estado_solicitacao = 'SUBMETIDA'                                     " +
    "            )                                                                          " +
    "    )                                                                                  " +
    "ORDER BY YEAR(r.data_requisicao) DESC, r.data_requisicao ASC                           ";

    public static final String SELECT_ALL_REQUISICAOS_SUBMETIDAS_TO_UNIDADE = 
    "SELECT DISTINCT r.* FROM requisicao r                                                  " +
    "    INNER JOIN requisicao_solicitacoes rs   ON rs.requisicao_id = r.id                 " +
    "    INNER JOIN solicitacao s                ON s.id             = rs.solicitacoes_id   " +
    "WHERE                                                                                  " +
    "    r.unidade_id        = :unidade_id            AND                                   " +
    "    r.estado_requisicao = 'SUBMETIDA'   AND                                            " +
    "    (                                                                                  " +
    "        (                                                                              " +
    "            s.tipo_solicitacao  = 'DIARIA'          OR                                 " +
    "            s.tipo_solicitacao  = 'PASSAGEM'                                           " +
    "        )                                           AND                                " +
    "        s.estado_solicitacao    = 'SUBMETIDA'                                          " +
    "    )                                                                                  " +
    "    AND NOT EXISTS (                                                                   " +
    "        SELECT s.id FROM solicitacao s                                                 " +
    "            INNER JOIN requisicao_solicitacoes rs       ON                             " +
    "                s.id = rs.solicitacoes_id                                              " +
    "        WHERE                                                                          " +
    "            rs.requisicao_id    = r.id                  AND                            " +
    "            s.tipo_solicitacao  = 'TRANSPORTE'          AND                            " +
    "            (                                                                          " +
    "                s.estado_solicitacao = 'RECUSADA'       OR                             " +
    "                s.estado_solicitacao = 'PREENCHENDO'    OR                             " +
    "                s.estado_solicitacao = 'SUBMETIDA'                                     " +
    "            )                                                                          " +
    "    )                                                                                  " +
    "ORDER BY YEAR(r.data_requisicao) DESC, r.data_requisicao ASC                           ";

    
    public static final String SELECT_REQUISICAOS_TO_UNIDADE_BY_STATUS = 
    "SELECT DISTINCT r.* FROM requisicao r                                                  " +
    "    INNER JOIN requisicao_solicitacoes rs   ON rs.requisicao_id = r.id                 " +
    "    INNER JOIN solicitacao s                ON s.id             = rs.solicitacoes_id   " +
    "WHERE                                                                                  " +
    "    r.unidade_id        = :unidade_id            AND                                   " +
    "    r.estado_requisicao = 'SUBMETIDA'   AND                                            " +
    "    (                                                                                  " +
    "        (                                                                              " +
    "            s.tipo_solicitacao  = 'DIARIA'          OR                                 " +
    "            s.tipo_solicitacao  = 'PASSAGEM'                                           " +
    "        )                                           AND                                " +
    "        s.estado_solicitacao    LIKE :estado_solicitacao                               " +
    "    )                                                                                  " +
    "    AND NOT EXISTS (                                                                   " +
    "        SELECT s.id FROM solicitacao s                                                 " +
    "            INNER JOIN requisicao_solicitacoes rs       ON                             " +
    "                s.id = rs.solicitacoes_id                                              " +
    "        WHERE                                                                          " +
    "            rs.requisicao_id    = r.id                  AND                            " +
    "            s.tipo_solicitacao  = 'TRANSPORTE'          AND                            " +
    "            (                                                                          " +
    "                s.estado_solicitacao = 'RECUSADA'       OR                             " +
    "                s.estado_solicitacao = 'PREENCHENDO'    OR                             " +
    "                s.estado_solicitacao = 'SUBMETIDA'                                     " +
    "            )                                                                          " +
    "    )                                                                                  " +
    "ORDER BY YEAR(r.data_requisicao) DESC, r.data_requisicao ASC                           ";
    
    
    public static final String SELECT_ALL_REQUISICAOS_BY_ID_TO_UNIDADE = 
    "SELECT DISTINCT r.* FROM requisicao r                                                  " +
    "    INNER JOIN requisicao_solicitacoes rs   ON rs.requisicao_id = r.id                 " +
    "    INNER JOIN solicitacao s                ON s.id             = rs.solicitacoes_id   " +
    "WHERE                                                                                  " +
    "    r.unidade_id        = :unidade_id            AND                                   " +
    "    r.estado_requisicao = 'SUBMETIDA'   AND                                            " +
    "    (                                                                                  " +
    "        (                                                                              " +
    "            s.tipo_solicitacao  = 'DIARIA'          OR                                 " +
    "            s.tipo_solicitacao  = 'PASSAGEM'                                           " +
    "        )                                           AND                                " +
    "        s.estado_solicitacao    != 'PREENCHENDO'                                       " +
    "    )                                                                                  " +
    "    AND NOT EXISTS (                                                                   " +
    "        SELECT s.id FROM solicitacao s                                                 " +
    "            INNER JOIN requisicao_solicitacoes rs       ON                             " +
    "                s.id = rs.solicitacoes_id                                              " +
    "        WHERE                                                                          " +
    "            rs.requisicao_id    = r.id                  AND                            " +
    "            s.tipo_solicitacao  = 'TRANSPORTE'          AND                            " +
    "            (                                                                          " +
    "                s.estado_solicitacao = 'RECUSADA'       OR                             " +
    "                s.estado_solicitacao = 'PREENCHENDO'    OR                             " +
    "                s.estado_solicitacao = 'SUBMETIDA'                                     " +
    "            )                                                                          " +
    "    )                                                                                  " +
    "    AND r.id = :requisicao_id                                                          " +
    "ORDER BY YEAR(r.data_requisicao) DESC, r.data_requisicao ASC                           ";
    
    
    public static final String SELECT_ALL_REQUISICAOS_BY_PROPOSTO_TO_UNIDADE = 
    "SELECT DISTINCT r.* FROM requisicao r                                                  " +
    "    INNER JOIN requisicao_solicitacoes rs   ON rs.requisicao_id = r.id                 " +
    "    INNER JOIN solicitacao s                ON s.id             = rs.solicitacoes_id   " +
    "    INNER JOIN pessoa p                     ON p.id             = r.proposto_id        " +
    "WHERE                                                                                  " +
    "    r.unidade_id        = :unidade_id            AND                                   " +
    "    p.nome              LIKE %:proposto%       AND                                     " +
    "    r.estado_requisicao = 'SUBMETIDA'   AND                                            " +
    "    (                                                                                  " +
    "        (                                                                              " +
    "            s.tipo_solicitacao  = 'DIARIA'          OR                                 " +
    "            s.tipo_solicitacao  = 'PASSAGEM'                                           " +
    "        )                                           AND                                " +
    "        s.estado_solicitacao    != 'PREENCHENDO'                                       " +
    "    )                                                                                  " +
    "    AND NOT EXISTS (                                                                   " +
    "        SELECT s.id FROM solicitacao s                                                 " +
    "            INNER JOIN requisicao_solicitacoes rs       ON                             " +
    "                s.id = rs.solicitacoes_id                                              " +
    "        WHERE                                                                          " +
    "            rs.requisicao_id    = r.id                  AND                            " +
    "            s.tipo_solicitacao  = 'TRANSPORTE'          AND                            " +
    "            (                                                                          " +
    "                s.estado_solicitacao = 'RECUSADA'       OR                             " +
    "                s.estado_solicitacao = 'PREENCHENDO'    OR                             " +
    "                s.estado_solicitacao = 'SUBMETIDA'                                     " +
    "            )                                                                          " +
    "    )                                                                                  " +
    "ORDER BY YEAR(r.data_requisicao) DESC, r.data_requisicao ASC                           ";
    
    public static final String SELECT_COUNT_RESULTS_BY_STATUS_ANO = 
    "SELECT COUNT(DISTINCT r.id) FROM requisicao r                                          " +
    "    INNER JOIN requisicao_solicitacoes rs   ON rs.requisicao_id = r.id                 " +
    "    INNER JOIN solicitacao s                ON s.id             = rs.solicitacoes_id   " +
    "WHERE                                                                                  " +
    "    r.unidade_id        = :unidade_id            AND                                   " +
    "    YEAR(r.data_requisicao) = :ano               AND                                   " +
    "    r.estado_requisicao = 'SUBMETIDA'   AND                                            " +
    "    (                                                                                  " +
    "        (                                                                              " +
    "            s.tipo_solicitacao  = 'DIARIA'          OR                                 " +
    "            s.tipo_solicitacao  = 'PASSAGEM'                                           " +
    "        )                                           AND                                " +
    "        s.estado_solicitacao    LIKE :estado_solicitacao                               " +
    "    )                                                                                  " +
    "    AND NOT EXISTS (                                                                   " +
    "        SELECT s.id FROM solicitacao s                                                 " +
    "            INNER JOIN requisicao_solicitacoes rs       ON                             " +
    "                s.id = rs.solicitacoes_id                                              " +
    "        WHERE                                                                          " +
    "            rs.requisicao_id    = r.id                  AND                            " +
    "            s.tipo_solicitacao  = 'TRANSPORTE'          AND                            " +
    "            (                                                                          " +
    "                s.estado_solicitacao = 'RECUSADA'       OR                             " +
    "                s.estado_solicitacao = 'PREENCHENDO'    OR                             " +
    "                s.estado_solicitacao = 'SUBMETIDA'                                     " +
    "            )                                                                          " +
    "    )                                                                                  " +
    "ORDER BY YEAR(r.data_requisicao) DESC, r.data_requisicao ASC                           ";
    
    public static final String SELECT_COUNT_RESULTS_BY_STATUS_DATA = 
    "SELECT COUNT(DISTINCT r.id) FROM requisicao r                                          " +
    "    INNER JOIN requisicao_solicitacoes rs   ON rs.requisicao_id = r.id                 " +
    "    INNER JOIN solicitacao s                ON s.id             = rs.solicitacoes_id   " +
    "WHERE                                                                                  " +
    "    r.unidade_id        = :unidade_id            AND                                   " +
    "    r.data_requisicao   = :data                  AND                                   " +
    "    r.estado_requisicao = 'SUBMETIDA'   AND                                            " +
    "    (                                                                                  " +
    "        (                                                                              " +
    "            s.tipo_solicitacao  = 'DIARIA'          OR                                 " +
    "            s.tipo_solicitacao  = 'PASSAGEM'                                           " +
    "        )                                           AND                                " +
    "        s.estado_solicitacao    LIKE :estado_solicitacao                               " +
    "    )                                                                                  " +
    "    AND NOT EXISTS (                                                                   " +
    "        SELECT s.id FROM solicitacao s                                                 " +
    "            INNER JOIN requisicao_solicitacoes rs       ON                             " +
    "                s.id = rs.solicitacoes_id                                              " +
    "        WHERE                                                                          " +
    "            rs.requisicao_id    = r.id                  AND                            " +
    "            s.tipo_solicitacao  = 'TRANSPORTE'          AND                            " +
    "            (                                                                          " +
    "                s.estado_solicitacao = 'RECUSADA'       OR                             " +
    "                s.estado_solicitacao = 'PREENCHENDO'    OR                             " +
    "                s.estado_solicitacao = 'SUBMETIDA'                                     " +
    "            )                                                                          " +
    "    )                                                                                  " +
    "ORDER BY YEAR(r.data_requisicao) DESC, r.data_requisicao ASC                           ";
    
    public static final String SELECT_COUNT_RESULTS_BY_STATUS_PERIODO = 
    "SELECT COUNT(DISTINCT r.id) FROM requisicao r                                          " +
    "    INNER JOIN requisicao_solicitacoes rs   ON rs.requisicao_id = r.id                 " +
    "    INNER JOIN solicitacao s                ON s.id             = rs.solicitacoes_id   " +
    "WHERE                                                                                  " +
    "    r.unidade_id        = :unidade_id            AND                                   " +
    "    (r.data_requisicao BETWEEN :from AND :to)    AND                                   " +
    "    r.estado_requisicao = 'SUBMETIDA'   AND                                            " +
    "    (                                                                                  " +
    "        (                                                                              " +
    "            s.tipo_solicitacao  = 'DIARIA'          OR                                 " +
    "            s.tipo_solicitacao  = 'PASSAGEM'                                           " +
    "        )                                           AND                                " +
    "        s.estado_solicitacao    LIKE :estado_solicitacao                               " +
    "    )                                                                                  " +
    "    AND NOT EXISTS (                                                                   " +
    "        SELECT s.id FROM solicitacao s                                                 " +
    "            INNER JOIN requisicao_solicitacoes rs       ON                             " +
    "                s.id = rs.solicitacoes_id                                              " +
    "        WHERE                                                                          " +
    "            rs.requisicao_id    = r.id                  AND                            " +
    "            s.tipo_solicitacao  = 'TRANSPORTE'          AND                            " +
    "            (                                                                          " +
    "                s.estado_solicitacao = 'RECUSADA'       OR                             " +
    "                s.estado_solicitacao = 'PREENCHENDO'    OR                             " +
    "                s.estado_solicitacao = 'SUBMETIDA'                                     " +
    "            )                                                                          " +
    "    )                                                                                  " +
    "ORDER BY YEAR(r.data_requisicao) DESC, r.data_requisicao ASC                           ";
    
    public static final String SELECT_COUNT_RESULTS_BY_ANO = 
    "SELECT COUNT(DISTINCT r.id) FROM requisicao r                                          " +
    "    INNER JOIN requisicao_solicitacoes rs   ON rs.requisicao_id = r.id                 " +
    "    INNER JOIN solicitacao s                ON s.id             = rs.solicitacoes_id   " +
    "WHERE                                                                                  " +
    "    r.unidade_id        = :unidade_id            AND                                   " +
    "    YEAR(r.data_requisicao) = :ano               AND                                   " +
    "    r.estado_requisicao = 'SUBMETIDA'   AND                                            " +
    "    (                                                                                  " +
    "        (                                                                              " +
    "            s.tipo_solicitacao  = 'DIARIA'          OR                                 " +
    "            s.tipo_solicitacao  = 'PASSAGEM'                                           " +
    "        )                                                                              " +
    "    )                                                                                  " +
    "    AND NOT EXISTS (                                                                   " +
    "        SELECT s.id FROM solicitacao s                                                 " +
    "            INNER JOIN requisicao_solicitacoes rs       ON                             " +
    "                s.id = rs.solicitacoes_id                                              " +
    "        WHERE                                                                          " +
    "            rs.requisicao_id    = r.id                  AND                            " +
    "            s.tipo_solicitacao  = 'TRANSPORTE'          AND                            " +
    "            (                                                                          " +
    "                s.estado_solicitacao = 'RECUSADA'       OR                             " +
    "                s.estado_solicitacao = 'PREENCHENDO'    OR                             " +
    "                s.estado_solicitacao = 'SUBMETIDA'                                     " +
    "            )                                                                          " +
    "    )                                                                                  " +
    "ORDER BY YEAR(r.data_requisicao) DESC, r.data_requisicao ASC                           ";
    
    public static final String SELECT_COUNT_RESULTS_BY_DATA = 
    "SELECT COUNT(DISTINCT r.id) FROM requisicao r                                          " +
    "    INNER JOIN requisicao_solicitacoes rs   ON rs.requisicao_id = r.id                 " +
    "    INNER JOIN solicitacao s                ON s.id             = rs.solicitacoes_id   " +
    "WHERE                                                                                  " +
    "    r.unidade_id        = :unidade_id            AND                                   " +
    "    r.data_requisicao   = :data                  AND                                   " +
    "    r.estado_requisicao = 'SUBMETIDA'   AND                                            " +
    "    (                                                                                  " +
    "        (                                                                              " +
    "            s.tipo_solicitacao  = 'DIARIA'          OR                                 " +
    "            s.tipo_solicitacao  = 'PASSAGEM'                                           " +
    "        )                                                                              " +
    "    )                                                                                  " +
    "    AND NOT EXISTS (                                                                   " +
    "        SELECT s.id FROM solicitacao s                                                 " +
    "            INNER JOIN requisicao_solicitacoes rs       ON                             " +
    "                s.id = rs.solicitacoes_id                                              " +
    "        WHERE                                                                          " +
    "            rs.requisicao_id    = r.id                  AND                            " +
    "            s.tipo_solicitacao  = 'TRANSPORTE'          AND                            " +
    "            (                                                                          " +
    "                s.estado_solicitacao = 'RECUSADA'       OR                             " +
    "                s.estado_solicitacao = 'PREENCHENDO'    OR                             " +
    "                s.estado_solicitacao = 'SUBMETIDA'                                     " +
    "            )                                                                          " +
    "    )                                                                                  " +
    "ORDER BY YEAR(r.data_requisicao) DESC, r.data_requisicao ASC                           ";
    
    public static final String SELECT_COUNT_RESULTS_BY_PERIODO = 
    "SELECT COUNT(DISTINCT r.id) FROM requisicao r                                          " +
    "    INNER JOIN requisicao_solicitacoes rs   ON rs.requisicao_id = r.id                 " +
    "    INNER JOIN solicitacao s                ON s.id             = rs.solicitacoes_id   " +
    "WHERE                                                                                  " +
    "    r.unidade_id        = :unidade_id            AND                                   " +
    "    (r.data_requisicao BETWEEN :from AND :to)    AND                                   " +
    "    r.estado_requisicao = 'SUBMETIDA'   AND                                            " +
    "    (                                                                                  " +
    "        (                                                                              " +
    "            s.tipo_solicitacao  = 'DIARIA'          OR                                 " +
    "            s.tipo_solicitacao  = 'PASSAGEM'                                           " +
    "        )                                                                              " +
    "    )                                                                                  " +
    "    AND NOT EXISTS (                                                                   " +
    "        SELECT s.id FROM solicitacao s                                                 " +
    "            INNER JOIN requisicao_solicitacoes rs       ON                             " +
    "                s.id = rs.solicitacoes_id                                              " +
    "        WHERE                                                                          " +
    "            rs.requisicao_id    = r.id                  AND                            " +
    "            s.tipo_solicitacao  = 'TRANSPORTE'          AND                            " +
    "            (                                                                          " +
    "                s.estado_solicitacao = 'RECUSADA'       OR                             " +
    "                s.estado_solicitacao = 'PREENCHENDO'    OR                             " +
    "                s.estado_solicitacao = 'SUBMETIDA'                                     " +
    "            )                                                                          " +
    "    )                                                                                  " +
    "ORDER BY YEAR(r.data_requisicao) DESC, r.data_requisicao ASC                           ";
    
    public static final String SELECT_REQUISICAOS_BY_STATUS_ANO = 
    "SELECT DISTINCT r.* FROM requisicao r                                          " +
    "    INNER JOIN requisicao_solicitacoes rs   ON rs.requisicao_id = r.id                 " +
    "    INNER JOIN solicitacao s                ON s.id             = rs.solicitacoes_id   " +
    "WHERE                                                                                  " +
    "    r.unidade_id        = :unidade_id            AND                                   " +
    "    YEAR(r.data_requisicao) = :ano               AND                                   " +
    "    r.estado_requisicao = 'SUBMETIDA'   AND                                            " +
    "    (                                                                                  " +
    "        (                                                                              " +
    "            s.tipo_solicitacao  = 'DIARIA'          OR                                 " +
    "            s.tipo_solicitacao  = 'PASSAGEM'                                           " +
    "        )                                           AND                                " +
    "        s.estado_solicitacao    LIKE :estado_solicitacao                               " +
    "    )                                                                                  " +
    "    AND NOT EXISTS (                                                                   " +
    "        SELECT s.id FROM solicitacao s                                                 " +
    "            INNER JOIN requisicao_solicitacoes rs       ON                             " +
    "                s.id = rs.solicitacoes_id                                              " +
    "        WHERE                                                                          " +
    "            rs.requisicao_id    = r.id                  AND                            " +
    "            s.tipo_solicitacao  = 'TRANSPORTE'          AND                            " +
    "            (                                                                          " +
    "                s.estado_solicitacao = 'RECUSADA'       OR                             " +
    "                s.estado_solicitacao = 'PREENCHENDO'    OR                             " +
    "                s.estado_solicitacao = 'SUBMETIDA'                                     " +
    "            )                                                                          " +
    "    )                                                                                  " +
    "ORDER BY YEAR(r.data_requisicao) DESC, r.data_requisicao ASC                           ";
    
    public static final String SELECT_REQUISICAOS_BY_STATUS_DATA = 
    "SELECT DISTINCT r.* FROM requisicao r                                          " +
    "    INNER JOIN requisicao_solicitacoes rs   ON rs.requisicao_id = r.id                 " +
    "    INNER JOIN solicitacao s                ON s.id             = rs.solicitacoes_id   " +
    "WHERE                                                                                  " +
    "    r.unidade_id        = :unidade_id            AND                                   " +
    "    r.data_requisicao   = :data                  AND                                   " +
    "    r.estado_requisicao = 'SUBMETIDA'   AND                                            " +
    "    (                                                                                  " +
    "        (                                                                              " +
    "            s.tipo_solicitacao  = 'DIARIA'          OR                                 " +
    "            s.tipo_solicitacao  = 'PASSAGEM'                                           " +
    "        )                                           AND                                " +
    "        s.estado_solicitacao    LIKE :estado_solicitacao                               " +
    "    )                                                                                  " +
    "    AND NOT EXISTS (                                                                   " +
    "        SELECT s.id FROM solicitacao s                                                 " +
    "            INNER JOIN requisicao_solicitacoes rs       ON                             " +
    "                s.id = rs.solicitacoes_id                                              " +
    "        WHERE                                                                          " +
    "            rs.requisicao_id    = r.id                  AND                            " +
    "            s.tipo_solicitacao  = 'TRANSPORTE'          AND                            " +
    "            (                                                                          " +
    "                s.estado_solicitacao = 'RECUSADA'       OR                             " +
    "                s.estado_solicitacao = 'PREENCHENDO'    OR                             " +
    "                s.estado_solicitacao = 'SUBMETIDA'                                     " +
    "            )                                                                          " +
    "    )                                                                                  " +
    "ORDER BY YEAR(r.data_requisicao) DESC, r.data_requisicao ASC                           ";
    
    public static final String SELECT_REQUISICAOS_BY_STATUS_PERIODO = 
    "SELECT DISTINCT r.* FROM requisicao r                                          " +
    "    INNER JOIN requisicao_solicitacoes rs   ON rs.requisicao_id = r.id                 " +
    "    INNER JOIN solicitacao s                ON s.id             = rs.solicitacoes_id   " +
    "WHERE                                                                                  " +
    "    r.unidade_id        = :unidade_id            AND                                   " +
    "    (r.data_requisicao BETWEEN :from AND :to)    AND                                   " +
    "    r.estado_requisicao = 'SUBMETIDA'   AND                                            " +
    "    (                                                                                  " +
    "        (                                                                              " +
    "            s.tipo_solicitacao  = 'DIARIA'          OR                                 " +
    "            s.tipo_solicitacao  = 'PASSAGEM'                                           " +
    "        )                                           AND                                " +
    "        s.estado_solicitacao    LIKE :estado_solicitacao                               " +
    "    )                                                                                  " +
    "    AND NOT EXISTS (                                                                   " +
    "        SELECT s.id FROM solicitacao s                                                 " +
    "            INNER JOIN requisicao_solicitacoes rs       ON                             " +
    "                s.id = rs.solicitacoes_id                                              " +
    "        WHERE                                                                          " +
    "            rs.requisicao_id    = r.id                  AND                            " +
    "            s.tipo_solicitacao  = 'TRANSPORTE'          AND                            " +
    "            (                                                                          " +
    "                s.estado_solicitacao = 'RECUSADA'       OR                             " +
    "                s.estado_solicitacao = 'PREENCHENDO'    OR                             " +
    "                s.estado_solicitacao = 'SUBMETIDA'                                     " +
    "            )                                                                          " +
    "    )                                                                                  " +
    "ORDER BY YEAR(r.data_requisicao) DESC, r.data_requisicao ASC                           ";
    
    public static final String SELECT_REQUISICAOS_BY_ANO = 
    "SELECT DISTINCT r.* FROM requisicao r                                          " +
    "    INNER JOIN requisicao_solicitacoes rs   ON rs.requisicao_id = r.id                 " +
    "    INNER JOIN solicitacao s                ON s.id             = rs.solicitacoes_id   " +
    "WHERE                                                                                  " +
    "    r.unidade_id        = :unidade_id            AND                                   " +
    "    YEAR(r.data_requisicao) = :ano               AND                                   " +
    "    r.estado_requisicao = 'SUBMETIDA'   AND                                            " +
    "    (                                                                                  " +
    "        (                                                                              " +
    "            s.tipo_solicitacao  = 'DIARIA'          OR                                 " +
    "            s.tipo_solicitacao  = 'PASSAGEM'                                           " +
    "        )                                                                              " +
    "    )                                                                                  " +
    "    AND NOT EXISTS (                                                                   " +
    "        SELECT s.id FROM solicitacao s                                                 " +
    "            INNER JOIN requisicao_solicitacoes rs       ON                             " +
    "                s.id = rs.solicitacoes_id                                              " +
    "        WHERE                                                                          " +
    "            rs.requisicao_id    = r.id                  AND                            " +
    "            s.tipo_solicitacao  = 'TRANSPORTE'          AND                            " +
    "            (                                                                          " +
    "                s.estado_solicitacao = 'RECUSADA'       OR                             " +
    "                s.estado_solicitacao = 'PREENCHENDO'    OR                             " +
    "                s.estado_solicitacao = 'SUBMETIDA'                                     " +
    "            )                                                                          " +
    "    )                                                                                  " +
    "ORDER BY YEAR(r.data_requisicao) DESC, r.data_requisicao ASC                           ";
    
    public static final String SELECT_REQUISICAOS_BY_DATA = 
    "SELECT DISTINCT r.* FROM requisicao r                                          " +
    "    INNER JOIN requisicao_solicitacoes rs   ON rs.requisicao_id = r.id                 " +
    "    INNER JOIN solicitacao s                ON s.id             = rs.solicitacoes_id   " +
    "WHERE                                                                                  " +
    "    r.unidade_id        = :unidade_id            AND                                   " +
    "    r.data_requisicao   = :data                  AND                                   " +
    "    r.estado_requisicao = 'SUBMETIDA'   AND                                            " +
    "    (                                                                                  " +
    "        (                                                                              " +
    "            s.tipo_solicitacao  = 'DIARIA'          OR                                 " +
    "            s.tipo_solicitacao  = 'PASSAGEM'                                           " +
    "        )                                                                              " +
    "    )                                                                                  " +
    "    AND NOT EXISTS (                                                                   " +
    "        SELECT s.id FROM solicitacao s                                                 " +
    "            INNER JOIN requisicao_solicitacoes rs       ON                             " +
    "                s.id = rs.solicitacoes_id                                              " +
    "        WHERE                                                                          " +
    "            rs.requisicao_id    = r.id                  AND                            " +
    "            s.tipo_solicitacao  = 'TRANSPORTE'          AND                            " +
    "            (                                                                          " +
    "                s.estado_solicitacao = 'RECUSADA'       OR                             " +
    "                s.estado_solicitacao = 'PREENCHENDO'    OR                             " +
    "                s.estado_solicitacao = 'SUBMETIDA'                                     " +
    "            )                                                                          " +
    "    )                                                                                  " +
    "ORDER BY YEAR(r.data_requisicao) DESC, r.data_requisicao ASC                           ";
    
    public static final String SELECT_REQUISICAOS_BY_PERIODO = 
    "SELECT DISTINCT r.* FROM requisicao r                                          " +
    "    INNER JOIN requisicao_solicitacoes rs   ON rs.requisicao_id = r.id                 " +
    "    INNER JOIN solicitacao s                ON s.id             = rs.solicitacoes_id   " +
    "WHERE                                                                                  " +
    "    r.unidade_id        = :unidade_id            AND                                   " +
    "    (r.data_requisicao BETWEEN :from AND :to)    AND                                   " +
    "    r.estado_requisicao = 'SUBMETIDA'   AND                                            " +
    "    (                                                                                  " +
    "        (                                                                              " +
    "            s.tipo_solicitacao  = 'DIARIA'          OR                                 " +
    "            s.tipo_solicitacao  = 'PASSAGEM'                                           " +
    "        )                                                                              " +
    "    )                                                                                  " +
    "    AND NOT EXISTS (                                                                   " +
    "        SELECT s.id FROM solicitacao s                                                 " +
    "            INNER JOIN requisicao_solicitacoes rs       ON                             " +
    "                s.id = rs.solicitacoes_id                                              " +
    "        WHERE                                                                          " +
    "            rs.requisicao_id    = r.id                  AND                            " +
    "            s.tipo_solicitacao  = 'TRANSPORTE'          AND                            " +
    "            (                                                                          " +
    "                s.estado_solicitacao = 'RECUSADA'       OR                             " +
    "                s.estado_solicitacao = 'PREENCHENDO'    OR                             " +
    "                s.estado_solicitacao = 'SUBMETIDA'                                     " +
    "            )                                                                          " +
    "    )                                                                                  " +
    "ORDER BY YEAR(r.data_requisicao) DESC, r.data_requisicao ASC                           ";
}

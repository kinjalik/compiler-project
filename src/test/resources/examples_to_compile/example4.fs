( prog (
    ( setq n 10 )
    ( setq b 2 )
    ( cond ( equal b 0) ( return 0 ) (
        ( setq res 1 )
        ( while ( lesseq b n ) (
            ( setq n ( divide n b ) )
            ( setq res ( plus res 1 ) )
          )
        )
        ( return res )
      )
    )
  )
)
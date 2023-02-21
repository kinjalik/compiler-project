( func dec ( i ) ( minus i 1 ) )

( func pow (arg1 arg2) (
    ( setq res1 )
    ( while ( nonequal arg2 0 ) (
        ( setq res ( times res arg1 ) )
        ( setq arg2 ( dec arg2 ) )
    ) )
    ( return res )
) )

( prog (
    ( return ( pow ( read 0 ) )
) )
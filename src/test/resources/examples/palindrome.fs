( func palindrom ( v ) (
    ( cond ( equal v 0 ) ( return 0 ) )
    ( setq acc 0 )
    ( setq rest v )
    ( while ( less acc rest ) (
        ( setq acc ( plus ( times acc 10 )
                          ( times rest 10 ) ) )
        ( setq rest ( divide rest 10 ) )
    ) )
    ( cond ( or ( equal acc rest )
           ( equal ( divide acc 10 ) rest ) )
        ( return 1 )
        ( return 0 )
    )
) )

( prog (
    ( return ( palindrom ( read 0 ) ) )
) )
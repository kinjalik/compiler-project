( prog (
  ( setq sum 0 )
  ( setq i 10 )
  ( while ( nonequal i 0 ) (
    ( setq sum ( plus sum i ) )
    ( setq i ( minus i 1 ) ) )
  )
  ( return sum ) )
)
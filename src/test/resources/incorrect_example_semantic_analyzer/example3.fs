(func sum(a)((return 1)))
(func sum(a)((return 2)))
(prog((return (plus (sum 0) (sum 1 2)))))
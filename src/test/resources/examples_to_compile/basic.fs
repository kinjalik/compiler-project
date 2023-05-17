(prog (
    (setq x 10)
    (setq y 0)
    (while (not (equal x 0)) (
    (setq y (plus y 1))
    (setq x (minus x 1))
    ))
    (setq z 0)
    (cond ((equal y 10)) ((setq z 1)))
    (return z)
))





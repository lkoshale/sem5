
( defun fib ( x )
  ( if ( = x 0 ) 0
   ( if ( = x 1 ) 1
  ( fib-iter 0 1 x )  ) ) )

  ( defun fib-iter ( a b x )
    ( if ( = x 1 ) b
    ( fib-iter b ( + a b ) ( - x 1 )  ) )
  )

( print ( fib 10 ) )

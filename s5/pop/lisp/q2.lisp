
( defun  len (  L  )
  ( if ( null L )
    0
    ( + 1  (len ( cdr L ) ) )
  )
)


( defun insert ( x L )
( if ( null L )
  (cons x nil )
  ( if ( > x ( car L ) )
    ( cons (car L ) ( insert x ( cdr L) ) )
    ( cons x L )
  )
)
)


( defun  sortList ( L )
 ( if (  OR (null L) ( = 1 (len L) ) )
    L
    ( if ( = 2 ( len L) )
    ( insert ( car L ) (cdr L) )
    ( insert ( car L ) ( sortList ( cdr L) ) )
    )
   )
)

( print ( sortList '( 96 314 5 87 12 3) ) )

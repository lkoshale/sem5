
( defun sbst ( a b L )
( if ( null L )
  L
  ( if ( atom ( car L ) )
    ( if ( eql a ( car L ) )
      ( cons b ( sbst a b (cdr L ) ) )
      ( cons ( car L ) ( sbst a b ( cdr L) ) )
    )

    ( cons ( sbst a b ( car L) ) ( sbst a b ( cdr L) ) )
  )
)

)

( print ( sbst 'a 'b '( a c d ( a (a a) a) a a c c) ) )

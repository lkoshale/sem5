


( defun  ln (  L  )
  ( if ( null L )
    0
    ( + 1  (ln ( cdr L ) ) )
  )
)



( defun mx ( x y z)

( if ( null z )
 0
( if ( > x y ) x y
)
)

)


( defun lxm ( a b L )

 ( if ( null L ) nil
  ( if ( = a (car L) )
    ( cons b  ( lxm  a b (cdr L) ) )
    ( cons (car L ) ( lxm a b ( cdr L) ) )
  )
 )

)


; a b L list of int
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

( defun ins ( x L )
( if ( null L )
  (cons x nil )
  ( if ( > x ( car L ) )
    ( cons (car L ) ( ins x ( cdr L) ) )
    ( cons x L )
  )
)
)


( defun  sortL ( L )
 ( if (  OR (null L) ( = 1 (ln L) ) )
    L
    ( if ( = 2 ( ln L) )
    ( ins ( car L ) (cdr L) )
    ( ins ( car L ) ( sortL ( cdr L) ) )
    )
   )
)

; ( defun trans ( lst )
;   ( if ( null ( car lst ) ) nil
;     ( mapcar #'rowtocol lst )
;    )
; )
(defun curry (function &rest args)
    (lambda (&rest more-args)
      (apply function (append args more-args))))


( defun base ( lst )

  ( mapcar ( curry #'playm lst ) () )
)

( defun playm (y)
    ( apply #'* y )
)





( defun trans (lstl)
  ( if ( = 1 ( length (car lstl) ) ) ( cons ( col lstl ) nil )
  ( cons ( col lstl) ( trans ( remain lstl) )  )  ))

( defun col( lstl)
    ( mapcar #'car lstl) )

( defun remain (lst)
 ( mapcar #'cdr lst )  )



;(print (trans '( ( 1 2  5) (3 4  6) ) ) )



( defun srt ( list )
  ( if ( = 1 ( length list ) ) list
    (ins (car list) (srt ( cdr list ) ) ) ) )

( defun ins ( x ls )
    ( if ( null ls ) (cons x nil)
     ( if ( > (length x) ( length (car ls) ) )
        ( cons (car ls) ( ins x ( cdr ls) ) )
        ( cons  x ls ) ) )  )

;(print (srt '( ( 1 2  8 8  8 ) (4 5 6) ) ) )
;( print ( cons nil nil ) );'( ( 1 2 ) (4 5 6) ) ) )

( defun fib ( x )
  ( if ( = x 0 ) 0
   ( if ( = x 1 ) 1
  ( fib-iter 0 1 x )  ) ) )

  ( defun fib-iter ( a b x )
    ( if ( = x 1 ) b
    ( fib-iter b ( + a b ) ( - x 1 )  ) )
  )

;( print ( fib 10 ) )


( defun memo-fun ( f )
  ( setf lst '() )
  ( lambda( &rest args )
    ( if ( exist args lst ) (getfun args lst)
     ( setf lst ( store args ( apply f args ) lst ) ) )

     (getfun args lst) ) )


  ( defun exist ( args  list )
    (; print list)
    ( if( null list ) nil
    ( if ( equal args ( cadr list ) ) t
      ( exist  args (cddr list) ) ) ) )

  ( defun getfun ( args  list)
        ( if( null list ) nil
        ( if ( equal args ( cadr list ) ) ( car list )
          ( getfun  args (cddr list) ) ) ) )

  ( defun store ( args x  list)
  ( setf list ( cons x ( cons args list )  ) )
  list )

( setf fk (memo-fun #'+  ) )
( print  (funcall fk 5 6 ) )
( print  (funcall fk 5 6 ) )


;(print (rowtocol  1 2 3  ) )
;(  sbst '5 '1 '2 '( 1  1 5  8 ) )
;( print ( ln '(1 1 1 10) 1 ) )
;( print ( sortL '( 96 314 5 87 12 3) ) )
;( print ( sbst 'a 'b '( a c d ( a (a a) a) a a c c) ) )
 ;( print ( sbst 1 2 '( (1 2 ) 1  (1 ( 8 1 1 ) 1 ) 3 2 3 ) ) )

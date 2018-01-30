
( defun srt ( list )
  ( if ( = 1 ( length list ) ) list
    (ins (car list) (srt ( cdr list ) ) ) ) )

( defun ins ( x ls )
    ( if ( null ls ) (cons x nil)
     ( if ( > (length x) ( length (car ls) ) )
        ( cons (car ls) ( ins x ( cdr ls) ) )
        ( cons  x ls ) ) )  )

(print (srt '( ( 1 2  8 8  8 ) (4 5 6) ) ) )


;-----------Q1 starts------------------

( defun sbst ( a b L )
( if ( null L )
  L
  ( if ( atom ( car L ) )  ; if atom then substitute
    ( if ( eql a ( car L ) )
      ( cons b ( sbst a b (cdr L ) ) )          ; if b
      ( cons ( car L ) ( sbst a b ( cdr L) ) )   ; if not b
    )

    ( cons ( sbst a b ( car L) ) ( sbst a b ( cdr L) ) )  ; interior list recur
  ) ) )

;----------------Q1 ends--------------------------------


;---------------Q2 starts-------------------------------

( defun lsort ( list )
  ( if ( = 1 ( length list ) ) list  ; only single element already sorted
    (ins (car list) (lsort ( cdr list ) ) ) ) )   ; sort the remaining and insert the element in its place sorted

; insert the element in its place sorted
( defun ins ( x ls )
    ( if ( null ls ) (cons x nil)
     ( if ( > (length x) ( length (car ls) ) )
        ( cons (car ls) ( ins x ( cdr ls) ) )
        ( cons  x ls ) ) )  )


;------------------Q2 ends --------------------------------------


;------------------Q3 starts -------------------------------

;computes length of list L
    ( defun  len (  L  )
      ( if ( null L )
        0
        ( + 1  (len ( cdr L ) ) )  )    )

; insert x in its placce in sorted list L
    ( defun insert ( x L )
    ( if ( null L )
      (cons x nil )
      ( if ( > x ( car L ) )
        ( cons (car L ) ( insert x ( cdr L) ) )
        ( cons x L )
      )   )  )

; recursively insertion sort
    ( defun  sortList ( L )
     ( if (  OR (null L) ( = 1 (len L) ) )
        L
        ( if ( = 2 ( len L) )
        ( insert ( car L ) (cdr L) )
        ( insert ( car L ) ( sortList ( cdr L) ) )
    ) ))

;------------------Q3 ends --------------------------------

;------------------Q4 starts -----------------------------

( defun trans (lstl)
  ( if ( = 1 ( length (car lstl) ) ) ( cons ( col lstl ) nil ) ; if ( (a) (b)) --> ( a b)
  ( cons ( col lstl) ( trans ( remain lstl) )  )  ))

( defun col( lstl)
    ( mapcar #'car lstl) )    ; takes a col and returns a row

( defun remain (lst)          ; removes first element from each row
 ( mapcar #'cdr lst )  )

;------------------Q4 ends --------------------------------

;-----------------Q5 starts --------------------------------

; return a memoised function
; uses list of format ( (return val) ( argument)  .... )
( defun memo-fun ( f )
  ( setf lst '() )              ;intialise list
  ( lambda( &rest args )
    ( if ( exist args lst ) (getfun args lst)           ; if args exist return val stored
     ( progn
       ;( print ( exist args lst ))
       ( setf lst ( store args ( apply f args ) lst ) )
       (getfun args lst) ) ) )  )    ; return the val


  ; check if args already exist in list
  ( defun exist ( args  list )
  ;  ( print list)
  ; ( print args )
    ( if( null list ) nil
    ( if ( equal args ( cadr list ) ) t
      ( exist  args (cddr list) ) ) ) )

  ; returns the r-val for the args
  ( defun getfun ( args  list)
        ( if( null list ) nil
        ( if ( equal args ( cadr list ) ) ( car list )
          ( getfun  args (cddr list) ) ) ) )

  ; store the val
  ( defun store ( args x  list)
  ( setf list ( cons x ( cons args list )  ) )
  list )

;------------------Q5 ends ------------------------------------

;------------------Q6 starts ----------------------------------

;tail recursion
( defun fib ( x )
  ( if ( = x 0 ) 0      ;base cases
   ( if ( = x 1 ) 1
  ( fib-iter 0 1 x )  ) ) )

  ( defun fib-iter ( a b x )
    ( if ( = x 1 ) b            ; if 1 return the fib sum
    ( fib-iter b ( + a b ) ( - x 1 )  ) )  ; reccur
  )

;--------------------Q6 ends ----------------------

;-----Queries on fn-------------------------------

;1
( print ( sbst 'a 'b '(a (a b c ) (a d) ) ) )
;2
( print (lsort '((A B C) (D E) (F G H) (D E) (I J K L) (M N) (O) ) ) )

;3
( print (sortList '( 10 6 2 3 4 8) ) )

;4
( print (trans '((a b) (c d)) ) )

;5 unomment p to test memoization in memo-fun
( setf fk (memo-fun #'+  ) )
( print  (funcall fk 5 6 ) )
( print  (funcall fk 5 6 ) )


;6
(print (fib  10) )

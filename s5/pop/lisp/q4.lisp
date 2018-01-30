
( defun trans (lstl)
  ( if ( = 1 ( length (car lstl) ) ) ( cons ( col lstl ) nil )
  ( cons ( col lstl) ( trans ( remain lstl) )  )  ))

( defun col( lstl)
    ( mapcar #'car lstl) )

( defun remain (lst)
 ( mapcar #'cdr lst )  )



(print (trans '( ( 1 2  5) (3 4  6) ( 10 11 12 )  ) ) )

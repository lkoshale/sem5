
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


( setf fk (memo-fun #'+  ) )
( print  (funcall fk 5 6 ) )
( print  (funcall fk 5 6 ) )

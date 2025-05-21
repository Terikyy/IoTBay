delete
from User
where UserID = '0';
delete
from Admin
where UserID = '0';
insert into User (UserID, Name, Email, Password)
VALUES ('0', 'SysAdmin', 'admin@iotbay.com', 'admin123');
insert into Admin (UserID)
VALUES ('0');
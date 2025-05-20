delete
from User
where UserID = '0';
delete
from Admin
where UserID = '0';
insert into User (UserID, Name, Email, Password, AddressID)
VALUES ('0', 'SysAdmin', 'admin@mail.com', 'admin123', null);
insert into Admin (UserID)
VALUES ('0');
delete
from User
where UserID = '187263';
delete
from Admin
where UserID = '187263';
insert into User (UserID, Name, Email, Password, AddressID)
VALUES ('187263', 'Admin', 'admin@mail.com', 'admin123', null);
insert into Admin (UserID)
VALUES ('187263');
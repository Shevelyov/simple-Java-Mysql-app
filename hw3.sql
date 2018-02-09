CREATE TABLE Artist (
	AName varchar(100) not null,
	Birthplace varchar(100),
	Age int,
	Style varchar(100),
	Primary Key (AName)
	);
    
    
CREATE TABLE Artwork (
	Title varchar(100) not null,
    Year int,
    Type varchar(100),
    Price double,
    AName varchar(100),
    Primary Key(Title),
    CONSTRAINT FK_artwork_artist_name FOREIGN KEY (AName) 
	REFERENCES Artist (AName) ON DELETE SET NULL ON UPDATE SET NULL
	);
    
CREATE TABLE CUSTOMER (
	CustId int not null,
    CName varchar(100),
    Address varchar(100),
    Amount double,
    Primary Key(CustId)
	);
    
CREATE TABLE AGROUP (
	GName varchar(100),
    PRIMARY KEY (GName)
    );

CREATE TABLE Classify (
	Title varchar(100),
    GName varchar(100),
    PRIMARY KEY (Title, GName),
	CONSTRAINT FK_classify_title FOREIGN KEY (Title) 
	REFERENCES Artwork (Title) ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT FK_classify_gname FOREIGN KEY (GName) 
	REFERENCES AGROUP (GName) ON DELETE RESTRICT ON UPDATE CASCADE
    );
    
CREATE TABLE LIKE_GROUP (
	CustID int not null,
    GName varchar(100) not null,
    Primary Key (CustId, GName),
    CONSTRAINT FK_likegroup_custId FOREIGN KEY (CustId)
    REFERENCES Customer (CustId) ON DELETE RESTRICT ON UPDATE CASCADE,
	CONSTRAINT FK_likegroup_gname FOREIGN KEY (GName)
    REFERENCES AGROUP (GName) ON DELETE RESTRICT ON UPDATE CASCADE
);

CREATE TABLE LIKE_ARTIST (
	CustID int,
    AName varchar(100),
    Primary Key (CustId, AName),
    CONSTRAINT FK_likeartist_custId FOREIGN KEY (CustId)
    REFERENCES Customer (CustId) ON DELETE RESTRICT ON UPDATE CASCADE,
	CONSTRAINT FK_likeartist_aname FOREIGN KEY (AName)
    REFERENCES Artist (AName) ON DELETE RESTRICT ON UPDATE CASCADE
); 
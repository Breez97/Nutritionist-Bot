import psycopg2

def create_table():
    connection = psycopg2.connect(
        dbname="DB_NAME",
        user="USERNAME",
        password="PASSWORD",
        host="localhost"
    )
    c = connection.cursor()
    c.execute('''CREATE TABLE IF NOT EXISTS nutrition_dishes
                 (image TEXT, title TEXT, calories TEXT, carbs TEXT, fat TEXT, protein TEXT, link TEXT)''')
    connection.commit()
    connection.close()


def insert_data(image, title, calories, carbs, fat, protein, link):
    connection = psycopg2.connect(
        dbname="DB_NAME",
        user="USERNAME",
        password="PASSWORD",
        host="localhost"
    )
    c = connection.cursor()
    c.execute('SELECT * FROM nutrition_dishes WHERE title=%s', (title,))
    row = c.fetchone()
    if row is None:
        c.execute(
            "INSERT INTO nutrition_dishes (image, title, calories, carbs, fat, protein, link) VALUES (%s, %s, %s, %s, %s, %s, %s)",
            (image, title, calories, carbs, fat, protein, link))
    connection.commit()
    connection.close()


if __name__ == '__main__':
    create_table()
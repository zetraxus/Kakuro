BOARD = [
[-1,40,240,-1,-1],
[12000,0,0,230,-1],
[14000,0,0,0,30],
[-1,18000,0,0,0],
[-1,-1,10000,0,0] ]

def get_row_sum(value):
    if (value < 10):
        return 0
    return int(value / 1000)

def get_column_sum(value):
    if (value < 10):
        return 0
    return int((value / 10) % 100)

def translate_board(board):
    height = len(board)
    width = len(board[0])

    results = ["%d %d" % (width, height)]

    for y, row in enumerate(board):
        for x, value in enumerate(row):
            # detect row sum
            rowsum = get_row_sum(value)
            if rowsum > 0:
                counted = 0
                for field in row[x+1:]:
                    if field == 0:
                        counted += 1
                    else:
                        break
                results.append("R %d %d %d %d" % (x, y, rowsum, counted))
            
            columnsum = get_column_sum(value)
            if columnsum > 0:
                counted = 0
                for new_y in range(y+1, height):
                    if board[new_y][x] == 0:
                        counted += 1 
                    else:
                        break
                results.append("C %d %d %d %d" % (x, y, columnsum, counted))
    
    return results

if __name__ == "__main__":
    print('\n'.join(translate_board(BOARD)))


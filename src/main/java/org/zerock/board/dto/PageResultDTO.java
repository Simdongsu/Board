package org.zerock.board.dto;

import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
// Generic Class 생성. DTO, EN은 PageResultDTO의 인스턴스를 생성할 때 지정.
public class PageResultDTO<DTO,EN> {
    private List<DTO> dtoList; //글목록을 저장하는 List
    private int totalPage; // 총페이지수
    private int page; //현재페이지
    private int size; //페이지당 보여지는 글수
    private int start; // 1......10 에서 시작번호
    private int end; // 1........10 에서 끝번호
    private boolean prev; //이전블록 유무 여부
    private boolean next; //다음블록 유무 여부

    private List<Integer> pageList; // 1......10 번호 목록
    //생성자
    public PageResultDTO(Page<EN> result, Function<EN,DTO> fn ){
        //목록생성. dtoList에 저장
        //map()함수에서 사용할 함수를 fn으로 지정
        //map함수 사용법
        //배열or컬렉션or페이지.stream().map(화살표함수).collect(Collectors.toList())
        dtoList = result.stream().map(fn).collect(Collectors.toList());
        //총페이지수
        totalPage = result.getTotalPages();
        //paging에 관련된 필드들의 값을 구해서 저장
        makePageList(result.getPageable());
    }
    //paging에 관련된 필드들의 값을 구해서 저장
    private void makePageList(Pageable pageable){
        this.page = pageable.getPageNumber() + 1; //페이지번호
        this.size = pageable.getPageSize(); //페이지당 글수
        System.out.println("페이지당 글수 @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ "+size);
        int tempEnd = (int)(Math.ceil(page/10.0)) * 10; // 1.......10에서 10. 총페이지수가 현재구간의 tempEnd보다 작으면 총페이지수가 end가 됨
        start=tempEnd-9; // 끝번호(10)에서 9를 빼면 시작번호(1)
        prev=start > 1; // 시작번호가 1보다 크면 최소 두번째 구간이므로 이전구간 존재
        end=totalPage>tempEnd?tempEnd:totalPage; //총페이지수가 현재구간의 tempEnd보다 작으면 총페이지수가 end가 됨
        next=totalPage>tempEnd; //총페이지수가 현재구간의 tempEnd보다 크면 다음 구간 존재
        pageList= IntStream.rangeClosed(start,end).boxed().collect(Collectors.toList());// 1....10까지의 수를 List에 저장
    }
}

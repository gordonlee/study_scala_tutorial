/*
  패턴 매칭은 데이터 구조에서 데이터를 뽑아내는 강력한 "절차(protocol)"다.

  정보를 정제된 방식으로 추출해야 하는 경우 패턴 매칭을 고려하라.
  노출 시키는 상태를 제어하기 위해 get/set을 활용하는 거소보다 unapply 메서드를 마음대로 바꿀 수 있으니
  자세한 구현은 숨기면서 정보를 외부에 노출 시킬 수 있다.
  case 절은 최대한 else(='위의 모든 case문에 해당하지 않는 경우')로 코드가 빠지지 않도록 최대한 방어하라.

 */